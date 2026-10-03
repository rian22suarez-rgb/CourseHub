package com.example.PRD.service.impl;

import com.example.PRD.dto.request.PurchaseTicketRequest;
import com.example.PRD.dto.response.TicketResponse;
import com.example.PRD.exception.BusinessRuleException;
import com.example.PRD.exception.ResourceNotFoundException;
import com.example.PRD.mapper.TicketMapper;
import com.example.PRD.model.*;
import com.example.PRD.repository.EventRepository;
import com.example.PRD.repository.TicketRepository;
import com.example.PRD.repository.UserRepository;
import com.example.PRD.service.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketMapper ticketMapper;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             UserRepository userRepository,
                             EventRepository eventRepository,
                             TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.ticketMapper = ticketMapper;
    }

    @Override
    @Transactional
    public TicketResponse purchase(PurchaseTicketRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.userEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userEmail()));
        if (!user.getActive()) {
            throw new BusinessRuleException("User is not active");
        }
        Event event = eventRepository.findByEventCode(request.eventCode())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + request.eventCode()));
        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new BusinessRuleException("Event is not available for purchase. Status: " + event.getStatus());
        }
        if (!event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new BusinessRuleException("Event date has already passed");
        }

        if (event.getMinimumAge() != null && event.getMinimumAge() > 0) {
            UserProfile profile = user.getProfile();
            if (profile == null || profile.getBirthDate() == null) {
                throw new BusinessRuleException("User profile or birth date missing");
            }
            int ageAtEvent = Period.between(profile.getBirthDate(), event.getEventDate().toLocalDate()).getYears();
            if (ageAtEvent < event.getMinimumAge()) {
                throw new BusinessRuleException("User does not meet minimum age: " + event.getMinimumAge());
            }
        }

        long paidCount = ticketRepository.countByEventEventCodeAndStatus(event.getEventCode(), TicketStatus.PAID);
        int capacity = event.getVenue().getCapacity();
        if (paidCount >= capacity) {
            throw new BusinessRuleException("Event is sold out");
        }

        BigDecimal price = calculatePrice(request.type());

        Ticket ticket = new Ticket();
        ticket.setTicketCode("TCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        ticket.setType(request.type());
        ticket.setPrice(price);
        ticket.setStatus(TicketStatus.PAID);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket.setUser(user);
        ticket.setEvent(event);
        ticketRepository.save(ticket);

        if (paidCount + 1 >= capacity) {
            event.setStatus(EventStatus.SOLD_OUT);
            eventRepository.save(event);
        }

        return ticketMapper.toResponse(ticket);
    }

    private BigDecimal calculatePrice(TicketType type) {
        BigDecimal base = new BigDecimal("100000");
        return switch (type) {
            case GENERAL   -> base;
            case STUDENT   -> base.multiply(new BigDecimal("0.7"));
            case VIP       -> base.multiply(new BigDecimal("2.5"));
            case BACKSTAGE -> base.multiply(new BigDecimal("4.0"));
        };
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse findByCode(String ticketCode) {
        return ticketRepository.findByTicketCode(ticketCode)
                .map(ticketMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> findByUserEmail(String email) {
        return ticketRepository.findByUserEmailIgnoreCaseOrderByPurchaseDateDesc(email)
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> findPaidTicketsByEvent(String eventCode) {
        return ticketRepository.findByEventEventCodeAndStatus(eventCode, TicketStatus.PAID)
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TicketResponse cancel(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketCode));
        if (ticket.getStatus() != TicketStatus.PAID) {
            throw new BusinessRuleException("Only PAID tickets can be cancelled. Current: " + ticket.getStatus());
        }
        if (ticket.getEvent().getEventDate().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("Cannot cancel after event date");
        }
        ticket.setStatus(TicketStatus.CANCELLED);
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public TicketResponse markAsUsed(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketCode));
        if (ticket.getStatus() != TicketStatus.PAID) {
            throw new BusinessRuleException("Only PAID tickets can be marked as used. Current: " + ticket.getStatus());
        }
        ticket.setStatus(TicketStatus.USED);
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }
}
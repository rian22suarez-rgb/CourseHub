package com.example.PRD.repository;

import com.example.PRD.model.Ticket;
import com.example.PRD.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketCode(String ticketCode);

    List<Ticket> findByUserEmailIgnoreCase(String email);

    List<Ticket> findByUserEmailIgnoreCaseAndStatus(String email, TicketStatus status);

    List<Ticket> findByUserEmailIgnoreCaseOrderByPurchaseDateDesc(String email);

    List<Ticket> findByEventEventCodeAndStatus(String eventCode, TicketStatus status);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.event.eventCode = :eventCode AND t.status = :status")
    long countByEventEventCodeAndStatus(@Param("eventCode") String eventCode,
                                        @Param("status") TicketStatus status);
}
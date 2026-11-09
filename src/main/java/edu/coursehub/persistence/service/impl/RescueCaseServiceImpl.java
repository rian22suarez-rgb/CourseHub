package edu.coursehub.persistence.service.impl;

import edu.coursehub.persistence.domain.RescueCase;
import edu.coursehub.persistence.domain.RescueStatus;
import edu.coursehub.persistence.dto.request.ChangeRescueStatusRequest;
import edu.coursehub.persistence.dto.response.RescueCaseResponse;
import edu.coursehub.persistence.exception.BusinessRuleException;
import edu.coursehub.persistence.exception.ResourceNotFoundException;
import edu.coursehub.persistence.mapper.RescueCaseMapper;
import edu.coursehub.persistence.repository.RescueCaseRepository;
import edu.coursehub.persistence.service.RescueCaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RescueCaseServiceImpl implements RescueCaseService {

    private final RescueCaseRepository rescueCaseRepository;
    private final RescueCaseMapper rescueCaseMapper;

    public RescueCaseServiceImpl(RescueCaseRepository rescueCaseRepository, RescueCaseMapper rescueCaseMapper) {
        this.rescueCaseRepository = rescueCaseRepository;
        this.rescueCaseMapper = rescueCaseMapper;
    }

    @Override
    public RescueCaseResponse findByCode(String caseCode) {
        RescueCase rescueCase = rescueCaseRepository.findByCaseCode(caseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Rescue case not found with code: " + caseCode));
        return rescueCaseMapper.toResponse(rescueCase);
    }

    @Override
    public List<RescueCaseResponse> findByStatus(RescueStatus status) {
        return rescueCaseRepository.findByStatus(status).stream()
                .map(rescueCaseMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public RescueCaseResponse changeStatus(String caseCode, ChangeRescueStatusRequest request) {
        RescueCase rescueCase = rescueCaseRepository.findByCaseCode(caseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Rescue case not found with code: " + caseCode));

        validateStateTransition(rescueCase.getStatus(), request.status());

        rescueCase.setStatus(request.status());
        RescueCase savedCase = rescueCaseRepository.save(rescueCase);
        return rescueCaseMapper.toResponse(savedCase);
    }

    private void validateStateTransition(RescueStatus currentStatus, RescueStatus newStatus) {
        boolean isValid = switch (currentStatus) {
            case ADMITTED -> newStatus == RescueStatus.UNDER_EVALUATION;
            case UNDER_EVALUATION -> newStatus == RescueStatus.IN_REHABILITATION;
            case IN_REHABILITATION -> newStatus == RescueStatus.READY_FOR_RELEASE;
            case READY_FOR_RELEASE -> newStatus == RescueStatus.RELEASED;
            case RELEASED -> false;
            default -> false;
        };

        if (!isValid) {
            throw new BusinessRuleException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }
}
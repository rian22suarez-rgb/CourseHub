package edu.coursehub.persistence.service;

import edu.coursehub.persistence.domain.RescueStatus;
import edu.coursehub.persistence.dto.request.ChangeRescueStatusRequest;
import edu.coursehub.persistence.dto.response.RescueCaseResponse;
import java.util.List;

public interface RescueCaseService {
    RescueCaseResponse findByCode(String caseCode);
    List<RescueCaseResponse> findByStatus(RescueStatus status);
    RescueCaseResponse changeStatus(String caseCode, ChangeRescueStatusRequest request);
}
package edu.coursehub.persistence.service;

import edu.coursehub.persistence.dto.request.CreateTreatmentRequest;
import edu.coursehub.persistence.dto.response.TreatmentResponse;
import java.util.List;

public interface TreatmentService {
    TreatmentResponse createTreatment(CreateTreatmentRequest request);
    List<TreatmentResponse> findByRescueCaseCode(String caseCode);
}
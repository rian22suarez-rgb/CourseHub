package edu.coursehub.persistence.dto.request;

import edu.coursehub.persistence.domain.TreatmentType;
import java.time.LocalDateTime;

public record CreateTreatmentRequest(
        String animalCode,
        String specialistCode,
        LocalDateTime performedAt,
        TreatmentType type,
        String description
) {
}

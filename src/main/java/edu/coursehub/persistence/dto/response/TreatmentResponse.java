package edu.coursehub.persistence.dto.response;

import edu.coursehub.persistence.domain.TreatmentType;
import java.time.LocalDateTime;

public record TreatmentResponse(
        Long id,
        String animalCode,
        String specialistCode,
        LocalDateTime performedAt,
        TreatmentType type,
        String description
) {
}
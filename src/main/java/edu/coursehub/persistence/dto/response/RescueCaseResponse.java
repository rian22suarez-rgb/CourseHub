package edu.coursehub.persistence.dto.response;

import edu.coursehub.persistence.domain.RescueStatus;
import java.time.LocalDate;

public record RescueCaseResponse(
        Long id,
        String caseCode,
        LocalDate rescueDate,
        String rescueLocation,
        RescueStatus status,
        String centerCode,
        String animalCode
) {
}
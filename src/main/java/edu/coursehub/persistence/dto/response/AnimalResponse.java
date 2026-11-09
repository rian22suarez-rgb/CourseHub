package edu.coursehub.persistence.dto.response;

import edu.coursehub.persistence.domain.AnimalSex;
import edu.coursehub.persistence.domain.RescueStatus;

public record AnimalResponse(
        Long id,
        String animalCode,
        String commonName,
        String scientificName,
        AnimalSex sex,
        String caseCode,
        RescueStatus rescueStatus
) {
}
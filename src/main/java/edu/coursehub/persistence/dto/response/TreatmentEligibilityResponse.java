package edu.coursehub.persistence.dto.response;

public record TreatmentEligibilityResponse(
        String animalCode,
        boolean eligible
) {
}
package edu.coursehub.persistence.mapper;

import edu.coursehub.persistence.domain.Animal;
import edu.coursehub.persistence.domain.Specialist;
import edu.coursehub.persistence.domain.Treatment;
import edu.coursehub.persistence.domain.TreatmentType;
import edu.coursehub.persistence.dto.response.TreatmentResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-14T20:08:04-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4.1 (Eclipse Adoptium)"
)
@Component
public class TreatmentMapperImpl implements TreatmentMapper {

    @Override
    public TreatmentResponse toResponse(Treatment treatment) {
        if ( treatment == null ) {
            return null;
        }

        String animalCode = null;
        String specialistCode = null;
        Long id = null;
        LocalDateTime performedAt = null;
        TreatmentType type = null;
        String description = null;

        animalCode = treatmentAnimalAnimalCode( treatment );
        specialistCode = treatmentSpecialistProfessionalCode( treatment );
        id = treatment.getId();
        performedAt = treatment.getPerformedAt();
        type = treatment.getType();
        description = treatment.getDescription();

        TreatmentResponse treatmentResponse = new TreatmentResponse( id, animalCode, specialistCode, performedAt, type, description );

        return treatmentResponse;
    }

    private String treatmentAnimalAnimalCode(Treatment treatment) {
        Animal animal = treatment.getAnimal();
        if ( animal == null ) {
            return null;
        }
        return animal.getAnimalCode();
    }

    private String treatmentSpecialistProfessionalCode(Treatment treatment) {
        Specialist specialist = treatment.getSpecialist();
        if ( specialist == null ) {
            return null;
        }
        return specialist.getProfessionalCode();
    }
}

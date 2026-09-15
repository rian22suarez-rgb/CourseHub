package edu.coursehub.persistence.mapper;

import edu.coursehub.persistence.domain.Animal;
import edu.coursehub.persistence.domain.RescueCase;
import edu.coursehub.persistence.domain.RescueCenter;
import edu.coursehub.persistence.domain.RescueStatus;
import edu.coursehub.persistence.dto.response.RescueCaseResponse;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-14T20:08:04-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4.1 (Eclipse Adoptium)"
)
@Component
public class RescueCaseMapperImpl implements RescueCaseMapper {

    @Override
    public RescueCaseResponse toResponse(RescueCase rescueCase) {
        if ( rescueCase == null ) {
            return null;
        }

        String centerCode = null;
        String animalCode = null;
        Long id = null;
        String caseCode = null;
        LocalDate rescueDate = null;
        String rescueLocation = null;
        RescueStatus status = null;

        centerCode = rescueCaseRescueCenterCode( rescueCase );
        animalCode = rescueCaseAnimalAnimalCode( rescueCase );
        id = rescueCase.getId();
        caseCode = rescueCase.getCaseCode();
        rescueDate = rescueCase.getRescueDate();
        rescueLocation = rescueCase.getRescueLocation();
        status = rescueCase.getStatus();

        RescueCaseResponse rescueCaseResponse = new RescueCaseResponse( id, caseCode, rescueDate, rescueLocation, status, centerCode, animalCode );

        return rescueCaseResponse;
    }

    private String rescueCaseRescueCenterCode(RescueCase rescueCase) {
        RescueCenter rescueCenter = rescueCase.getRescueCenter();
        if ( rescueCenter == null ) {
            return null;
        }
        return rescueCenter.getCode();
    }

    private String rescueCaseAnimalAnimalCode(RescueCase rescueCase) {
        Animal animal = rescueCase.getAnimal();
        if ( animal == null ) {
            return null;
        }
        return animal.getAnimalCode();
    }
}

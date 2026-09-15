package edu.coursehub.persistence.mapper;

import edu.coursehub.persistence.domain.Treatment;
import edu.coursehub.persistence.dto.response.TreatmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {

    @Mapping(target = "animalCode", source = "animal.animalCode")
    @Mapping(target = "specialistCode", source = "specialist.professionalCode")
    TreatmentResponse toResponse(Treatment treatment);
}
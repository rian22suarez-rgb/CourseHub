package edu.coursehub.persistence.mapper;

import edu.coursehub.persistence.domain.Animal;
import edu.coursehub.persistence.dto.response.AnimalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    @Mapping(target = "caseCode", source = "rescueCase.caseCode")
    @Mapping(target = "rescueStatus", source = "rescueCase.status")
    AnimalResponse toResponse(Animal animal);
}
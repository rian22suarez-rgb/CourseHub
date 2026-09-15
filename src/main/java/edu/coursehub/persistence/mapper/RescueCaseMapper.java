package edu.coursehub.persistence.mapper;

import edu.coursehub.persistence.domain.RescueCase;
import edu.coursehub.persistence.dto.response.RescueCaseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RescueCaseMapper {

    @Mapping(target = "centerCode", source = "rescueCenter.code")
    @Mapping(target = "animalCode", source = "animal.animalCode")
    RescueCaseResponse toResponse(RescueCase rescueCase);
}
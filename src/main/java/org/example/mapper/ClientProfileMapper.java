package org.example.mapper;

import org.example.dto.response.ClientProfileResponse;
import org.example.model.ClientProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientProfileMapper {

    ClientProfileResponse toResponse(ClientProfileEntity clientProfileEntity);
}

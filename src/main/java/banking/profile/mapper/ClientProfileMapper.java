package banking.profile.mapper;

import banking.profile.dto.response.ClientProfileResponse;
import banking.profile.model.ClientProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientProfileMapper {

    ClientProfileResponse toResponse(ClientProfileEntity clientProfileEntity);
}

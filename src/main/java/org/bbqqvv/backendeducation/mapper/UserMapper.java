package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.request.UserCreationRequest;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Ánh xạ UserCreationRequest thành User
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "authorities", expression = "java(java.util.Set.of(org.bbqqvv.backendeducation.entity.Role.ROLE_USER))")
    User toUser(UserCreationRequest request);

    // Ánh xạ User thành UserResponse
    @Mapping(target = "authorities", ignore = false)
    UserResponse toUserResponse(User user);
}

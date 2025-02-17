package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.request.UserCreationRequest;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "roles", expression = "java(java.util.Set.of(org.bbqqvv.backendeducation.entity.Role.valueOf(request.getRole())))") // Chuyển đổi String thành Enum Role
    User toUser(UserCreationRequest request);

    @Mapping(target = "role", expression = "java(user.getRoles().stream().map(role -> role.name()).collect(java.util.stream.Collectors.joining(\", \")))") // Chuyển Set<Role> về String
    UserResponse toUserResponse(User user);
}

package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.request.UserCreationRequest;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserProfileMapper.class)
public interface UserMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "roles", expression = "java(java.util.Set.of(org.bbqqvv.backendeducation.entity.Role.valueOf(request.getRole())))")
    User toUser(UserCreationRequest request);

    @Mapping(target = "role", expression = "java(user.getRoles().stream().map(role -> role.name()).collect(java.util.stream.Collectors.joining(\", \")))")
    @Mapping(target = "profile", source = "profile") // ánh xạ sang UserProfileResponse
    @Mapping(target = "id", source = "user.id")
    UserResponse toUserResponse(User user, UserProfile profile);
}

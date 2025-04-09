package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.request.UpdateProfileRequest;
import org.bbqqvv.backendeducation.dto.response.UserProfileResponse;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(UpdateProfileRequest updateProfileRequest);
    @Mapping(target = "id", source = "id")
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}

package org.bbqqvv.backendeducation.service;

import jakarta.validation.Valid;
import org.bbqqvv.backendeducation.dto.request.ChangePasswordRequest;
import org.bbqqvv.backendeducation.dto.request.UserCreationRequest;
import org.bbqqvv.backendeducation.dto.request.UserUpdateRequest;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.entity.User;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
    UserResponse getUserById(String id);
    UserResponse changePassword(String email, ChangePasswordRequest request);
//    UserResponse updateUserProfile(String email, UserUpdateRequest request);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(String id, UserCreationRequest request);
    void deleteUser(String id);
    User getUserByEmailEntity(String email);

    boolean existsByEmail(String email);
}


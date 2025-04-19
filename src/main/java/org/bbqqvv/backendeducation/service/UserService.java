package org.bbqqvv.backendeducation.service;

import org.bbqqvv.backendeducation.dto.request.ChangePasswordRequest;
import org.bbqqvv.backendeducation.dto.request.UpdateProfileRequest;
import org.bbqqvv.backendeducation.dto.request.UserCreationRequest;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.entity.User;

import java.util.List;
import java.util.Set;


public interface UserService {
    UserResponse createUser(UserCreationRequest request);
    UserResponse changePassword(String email, ChangePasswordRequest request);
    UserResponse updateUserProfile(String email, UpdateProfileRequest request);
    List<UserResponse> getAllUsers();
    void deleteUser(String id);
    User getUserByEmailEntity(String email);
    List<UserResponse> getClassmates(String email);
    List<UserResponse> getTeachersForClass(String className);
    boolean existsByEmail(String email);

    Set<String> getClassesTaughtByTeacher(String teacherEmail); // Đổi từ List -> Set

    List<UserResponse> getStudentsForTeacherClass(String teacherEmail, String className);

    UserResponse getCurrentUser(String username);
}
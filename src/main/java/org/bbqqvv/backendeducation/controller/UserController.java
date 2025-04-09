// UserController.java
package org.bbqqvv.backendeducation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.request.ChangePasswordRequest;
import org.bbqqvv.backendeducation.dto.request.UpdateProfileRequest;
import org.bbqqvv.backendeducation.dto.request.UserCreationRequest;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.createUser(request))
                .build();
    }

    @PutMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        userService.changePassword(userDetails.getUsername(), request);
        return ApiResponse.<String>builder()
                .data("Password updated successfully")
                .build();
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ApiResponse<UserResponse> updateProfile(@RequestBody UpdateProfileRequest request,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUserProfile(userDetails.getUsername(), request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getUserById(id))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getAllUsers())
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ApiResponse.<String>builder()
                .data("User has been deleted")
                .build();
    }

    @GetMapping("/classmates")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<List<UserResponse>> getClassmates(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getClassmates(userDetails.getUsername()))
                .build();
    }

    @GetMapping("/class/teachers")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<List<UserResponse>> getTeachersForMyClass(@AuthenticationPrincipal UserDetails userDetails) {
        String className = userService.getUserByEmailEntity(userDetails.getUsername()).getStudentClass();
        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getTeachersForClass(className))
                .build();
    }
}
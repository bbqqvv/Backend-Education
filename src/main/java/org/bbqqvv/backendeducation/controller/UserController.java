package org.bbqqvv.backendeducation.controller;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.request.ChangePasswordRequest;
import org.bbqqvv.backendeducation.dto.request.ChangeProfileRequest;
import org.bbqqvv.backendeducation.dto.request.UserCreationRequest;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Tạo người dùng mới
    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return ApiResponse.<UserResponse>builder()
                .data(userResponse)
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
//    @PutMapping("/profile")
//    public ApiResponse<UserResponse> updateProfile(@RequestBody ChangeProfileRequest request,
//                                                   @AuthenticationPrincipal UserDetails userDetails) {
//        UserResponse updatedUser = userService.updateUserProfile(userDetails.getUsername(), request);
//        return ApiResponse.<UserResponse>builder()
//                .data(updatedUser)
//                .build();
//    }

    // Lấy người dùng theo ID
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String id) {
        UserResponse userResponse = userService.getUserById(id);
        return ApiResponse.<UserResponse>builder()
                .data(userResponse)
                .build();
    }
    // Lấy tất cả người dùng
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponses = userService.getAllUsers();
        return ApiResponse.<List<UserResponse>>builder()
                .data(userResponses)
                .build();
    }

    // Cập nhật thông tin người dùng
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserCreationRequest request) {
        UserResponse userResponse = userService.updateUser(id, request);
        return ApiResponse.<UserResponse>builder()
                .data(userResponse)
                .build();
    }

    // Xóa người dùng
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ApiResponse.<String>builder()
                .data("User has been deleted")
                .build();
    }
}

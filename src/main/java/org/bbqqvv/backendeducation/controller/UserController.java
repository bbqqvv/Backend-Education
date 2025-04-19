// UserController.java
package org.bbqqvv.backendeducation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.request.ChangePasswordRequest;
import org.bbqqvv.backendeducation.dto.request.UpdateProfileRequest;
import org.bbqqvv.backendeducation.dto.response.UserResponse;
import org.bbqqvv.backendeducation.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


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


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getAllUsers())
                .build();
    }
    @GetMapping("/current-user")
    public ApiResponse<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getCurrentUser(userDetails.getUsername()))
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
                .success(true)
                .message("Lấy danh sách bạn cùng lớp thành công")
                .data(userService.getClassmates(userDetails.getUsername()))
                .build();
    }

    @GetMapping("/class/teachers")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<List<UserResponse>> getTeachersForMyClass(@AuthenticationPrincipal UserDetails userDetails) {
        String className = userService.getUserByEmailEntity(userDetails.getUsername()).getStudentClass();
        return ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .message("Lấy danh sách giáo viên của lớp thành công")
                .data(userService.getTeachersForClass(className))
                .build();
    }
    @GetMapping("/my-classes")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<Set<String>> getMyClasses(@AuthenticationPrincipal UserDetails userDetails) {
        String teacherEmail = userDetails.getUsername();
        return ApiResponse.<Set<String>>builder()
                .success(true)
                .message("Lấy danh sách lớp đang dạy thành công")
                .data(userService.getClassesTaughtByTeacher(teacherEmail))
                .build();
    }

    @GetMapping("/class/{className}/students")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<List<UserResponse>> getStudentsInClass(@PathVariable String className,
                                                              @AuthenticationPrincipal UserDetails userDetails) {
        String teacherEmail = userDetails.getUsername();
        return ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .message("Lấy danh sách học sinh thành công")
                .data(userService.getStudentsForTeacherClass(teacherEmail, className))
                .build();
    }

}
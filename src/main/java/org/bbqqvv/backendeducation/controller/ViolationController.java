package org.bbqqvv.backendeducation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.PageResponse;
import org.bbqqvv.backendeducation.dto.request.ViolationRequest;
import org.bbqqvv.backendeducation.dto.response.ViolationResponse;
import org.bbqqvv.backendeducation.service.ViolationService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/violations")
@RequiredArgsConstructor
public class ViolationController {

    private final ViolationService violationService;

    /**
     * Thêm vi phạm (chỉ Admin)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ViolationResponse> addViolation(@RequestBody @Valid ViolationRequest request) {
        ViolationResponse response = violationService.addViolation(request);
        return ApiResponse.<ViolationResponse>builder()
                .success(true)
                .message("Violation recorded successfully")
                .data(response)
                .build();
    }

    /**
     * Lấy danh sách vi phạm phân trang
     */
    @GetMapping
    public ApiResponse<PageResponse<ViolationResponse>> getAllViolations(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return ApiResponse.<PageResponse<ViolationResponse>>builder()
                .success(true)
                .message("List of violations")
                .data(violationService.getAllViolations(pageable))
                .build();
    }

    /**
     * Lấy chi tiết vi phạm theo ID
     */
    @GetMapping("/{id}")
    public ApiResponse<ViolationResponse> getViolationById(@PathVariable String id) {
        return ApiResponse.<ViolationResponse>builder()
                .success(true)
                .message("Violation detail")
                .data(violationService.getViolationById(id))
                .build();
    }

    /**
     * Xoá vi phạm theo ID (chỉ Admin)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteViolation(@PathVariable String id) {
        violationService.deleteViolation(id);
        return ApiResponse.<String>builder()
                .success(true)
                .message("Violation deleted")
                .data("Deleted: " + id)
                .build();
    }
    /**
     * Cập nhật thông tin vi phạm (chỉ Admin)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ViolationResponse> updateViolation(
            @PathVariable String id,
            @RequestBody @Valid ViolationRequest request) {

        ViolationResponse updated = violationService.updateViolation(id, request);
        return ApiResponse.<ViolationResponse>builder()
                .success(true)
                .message("Violation updated successfully")
                .data(updated)
                .build();
    }

    // 📌 Lấy danh sách vi phạm của người dùng hiện tại
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ApiResponse<PageResponse<ViolationResponse>> getMyViolations(@PageableDefault(size = 10) Pageable pageable) {
        PageResponse<ViolationResponse> violations = violationService.getCurrentUserViolations(pageable);
        return ApiResponse.<PageResponse<ViolationResponse>>builder()
                .success(true)
                .message("Fetched current user's violations")
                .data(violations)
                .build();
    }

}

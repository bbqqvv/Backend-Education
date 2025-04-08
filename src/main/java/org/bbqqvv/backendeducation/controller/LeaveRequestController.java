package org.bbqqvv.backendeducation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.request.LeaveRequestRequest;
import org.bbqqvv.backendeducation.dto.request.UpdateLeaveStatusRequest;
import org.bbqqvv.backendeducation.dto.response.LeaveRequestResponse;
import org.bbqqvv.backendeducation.service.LeaveRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<LeaveRequestResponse> createLeaveRequest(@RequestBody @Valid LeaveRequestRequest request) {
        return ApiResponse.<LeaveRequestResponse>builder()
                .success(true)
                .message("Đã gửi đơn báo nghỉ")
                .data(leaveRequestService.createLeaveRequest(request))
                .build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<LeaveRequestResponse> updateStatus(
            @PathVariable String id,
            @RequestBody @Valid UpdateLeaveStatusRequest request
    ) {
        return ApiResponse.<LeaveRequestResponse>builder()
                .success(true)
                .message("Cập nhật trạng thái đơn nghỉ thành công")
                .data(leaveRequestService.updateStatus(id, request))
                .build();
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<LeaveRequestResponse>> getAllLeaveRequests() {
        return ApiResponse.<List<LeaveRequestResponse>>builder()
                .success(true)
                .message("Danh sách tất cả đơn báo nghỉ")
                .data(leaveRequestService.getAllLeaveRequests())
                .build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<Void> deleteLeaveRequest(@PathVariable String id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Xoá đơn báo nghỉ thành công")
                .build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<List<LeaveRequestResponse>> getMyLeaveRequests() {
        return ApiResponse.<List<LeaveRequestResponse>>builder()
                .success(true)
                .message("Danh sách đơn báo nghỉ của giáo viên hiện tại")
                .data(leaveRequestService.getLeaveRequestsByCurrentTeacher())
                .build();
    }
}

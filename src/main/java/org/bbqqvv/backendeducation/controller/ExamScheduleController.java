package org.bbqqvv.backendeducation.controller;
import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;

import org.bbqqvv.backendeducation.dto.request.ExamScheduleRequest;
import org.bbqqvv.backendeducation.dto.response.ExamScheduleResponse;
import org.bbqqvv.backendeducation.service.ExamScheduleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamScheduleController {

    private final ExamScheduleService examScheduleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ExamScheduleResponse> create(@RequestBody ExamScheduleRequest request) {
        return ApiResponse.<ExamScheduleResponse>builder()
                .success(true)
                .message("Tạo lịch thi thành công")
                .data(examScheduleService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ExamScheduleResponse> update(@PathVariable String id, @RequestBody ExamScheduleRequest request) {
        return ApiResponse.<ExamScheduleResponse>builder()
                .success(true)
                .message("Cập nhật lịch thi thành công")
                .data(examScheduleService.update(id, request))
                .build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ApiResponse<List<ExamScheduleResponse>> getMySchedule() {
        return ApiResponse.<List<ExamScheduleResponse>>builder()
                .success(true)
                .message("Lịch thi/lịch coi thi của bạn")
                .data(examScheduleService.getMyExamSchedule())
                .build();
    }

    @GetMapping("/class/{className}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<ExamScheduleResponse>> getByClass(@PathVariable String className) {
        return ApiResponse.<List<ExamScheduleResponse>>builder()
                .success(true)
                .message("Lịch thi của lớp")
                .data(examScheduleService.getByClassName(className))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable String id) {
        examScheduleService.delete(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Xóa lịch thi thành công")
                .build();
    }
}

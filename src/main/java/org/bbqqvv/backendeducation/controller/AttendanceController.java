package org.bbqqvv.backendeducation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.request.AttendanceRequest;
import org.bbqqvv.backendeducation.dto.response.AttendanceResponse;
import org.bbqqvv.backendeducation.service.AttendanceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Validated
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/record")
    public ApiResponse<AttendanceResponse> record(@Valid @RequestBody AttendanceRequest req) {
        AttendanceResponse res = attendanceService.recordAttendance(req);
        return ApiResponse.<AttendanceResponse>builder()
                .success(true)
                .message("✅ Điểm danh thành công")
                .data(res)
                .build();
    }

    @PostMapping("/bulk-record")
    public ApiResponse<List<AttendanceResponse>> bulkRecord(@Valid @RequestBody List<AttendanceRequest> requests) {
        List<AttendanceResponse> res = attendanceService.recordAttendances(requests);
        return ApiResponse.<List<AttendanceResponse>>builder()
                .success(true)
                .message("✅ Điểm danh hàng loạt thành công")
                .data(res)
                .build();
    }
    @GetMapping("/class/{className}")
    public ApiResponse<List<AttendanceResponse>> getAttendanceByClass(@PathVariable String className) {
        List<AttendanceResponse> res = attendanceService.getAttendanceByClassName(className);
        return ApiResponse.<List<AttendanceResponse>>builder()
                .success(true)
                .message("✅ Lấy danh sách điểm danh theo lớp thành công")
                .data(res)
                .build();
    }
    @GetMapping("/student/{studentId}")
    public ApiResponse<List<AttendanceResponse>> getAttendanceByStudent(@PathVariable String studentId) {
        List<AttendanceResponse> res = attendanceService.getAttendanceByStudentId(studentId);
        return ApiResponse.<List<AttendanceResponse>>builder()
                .success(true)
                .message("✅ Lấy danh sách điểm danh theo học sinh thành công")
                .data(res)
                .build();
    }
    @GetMapping("/date-range")
    public ApiResponse<List<AttendanceResponse>> getAttendanceByDateRange(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr
    ) {
        LocalDateTime startDate = LocalDateTime.parse(startDateStr);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr);

        List<AttendanceResponse> res = attendanceService.getAttendanceByDateRange(startDate, endDate);
        return ApiResponse.<List<AttendanceResponse>>builder()
                .success(true)
                .message("✅ Lấy danh sách điểm danh theo khoảng ngày thành công")
                .data(res)
                .build();
    }

}

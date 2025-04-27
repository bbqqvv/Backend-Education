package org.bbqqvv.backendeducation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.request.AttendanceRequest;
import org.bbqqvv.backendeducation.dto.response.AttendanceResponse;
import org.bbqqvv.backendeducation.service.AttendanceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}

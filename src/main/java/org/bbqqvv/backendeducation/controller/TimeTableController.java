// TimeTableController.java
package org.bbqqvv.backendeducation.controller;
import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.request.TimeTableRequest;
import org.bbqqvv.backendeducation.dto.response.TimeTableResponse;
import org.bbqqvv.backendeducation.dto.response.WeeklyScheduleResponse;
import org.bbqqvv.backendeducation.service.TimeTableService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/timetables")
@RequiredArgsConstructor
public class TimeTableController {

    private final TimeTableService timeTableService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TimeTableResponse> createTimeTable(@RequestBody TimeTableRequest request) {
        return ApiResponse.<TimeTableResponse>builder()
                .success(true)
                .message("Tạo thời khóa biểu thành công")
                .data(timeTableService.create(request))
                .build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ApiResponse<List<TimeTableResponse>> getMyTimeTable() {
        return ApiResponse.<List<TimeTableResponse>>builder()
                .success(true)
                .message("Thời khóa biểu của lớp bạn")
                .data(timeTableService.getTimeTableForCurrentUser())
                .build();
    }

    @GetMapping("/class/{className}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<TimeTableResponse>> getByClass(@PathVariable String className) {
        return ApiResponse.<List<TimeTableResponse>>builder()
                .success(true)
                .message("Thời khóa biểu của lớp")
                .data(timeTableService.getByClassName(className))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteTimeTable(@PathVariable String id) {
        timeTableService.delete(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Xóa thời khóa biểu thành công")
                .build();
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TimeTableResponse> updateTimeTable(
            @PathVariable String id,
            @RequestBody TimeTableRequest request
    ) {
        return ApiResponse.<TimeTableResponse>builder()
                .success(true)
                .message("Cập nhật thời khóa biểu thành công")
                .data(timeTableService.update(id, request))
                .build();
    }
    @GetMapping("/class/{className}/weekly")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ApiResponse<WeeklyScheduleResponse> getWeeklySchedule(
            @PathVariable String className,
            @RequestParam("weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate
    ) {
        return ApiResponse.<WeeklyScheduleResponse>builder()
                .success(true)
                .message("Lịch học trong tuần của lớp " + className)
                .data(timeTableService.getWeeklySchedule(className, weekStartDate))
                .build();
    }

}
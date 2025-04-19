package org.bbqqvv.backendeducation.service.impl;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.config.jwt.SecurityUtils;
import org.bbqqvv.backendeducation.dto.request.TimeTableRequest;
import org.bbqqvv.backendeducation.dto.response.DailyScheduleResponse;
import org.bbqqvv.backendeducation.dto.response.TimeTableResponse;
import org.bbqqvv.backendeducation.dto.response.WeeklyScheduleResponse;
import org.bbqqvv.backendeducation.entity.*;
import org.bbqqvv.backendeducation.exception.AppException;
import org.bbqqvv.backendeducation.exception.ErrorCode;
import org.bbqqvv.backendeducation.mapper.TimeTableMapper;
import org.bbqqvv.backendeducation.repository.TimeTableRepository;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.service.TimeTableService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTableServiceImpl implements TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final UserRepository userRepository;
    private final TimeTableMapper timeTableMapper;

    @Override
    public TimeTableResponse create(TimeTableRequest request) {
        // Kiểm tra trùng lịch cho lớp và giáo viên
        checkConflict(request, null);

        // Tạo đối tượng TimeTable mới và lưu vào database
        TimeTable newEntry = timeTableMapper.toTimeTable(request);
        return timeTableMapper.toTimeTableResponse(timeTableRepository.save(newEntry));
    }

    @Override
    public TimeTableResponse update(String id, TimeTableRequest request) {
        // Kiểm tra xem lịch học đã tồn tại chưa
        timeTableRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TIME_TABLE_NOT_FOUND));

        // Kiểm tra trùng lịch cho lớp và giáo viên
        checkConflict(request, id);

        // Cập nhật thông tin lịch học
        TimeTable updated = timeTableMapper.toTimeTable(request).toBuilder().id(id).build();
        return timeTableMapper.toTimeTableResponse(timeTableRepository.save(updated));
    }

    private void checkConflict(TimeTableRequest request, String currentId) {
        // Kiểm tra lịch học của lớp
        List<TimeTable> classSchedules = timeTableRepository
                .findByClassNameAndDayOfWeek(request.getClassName(), request.getDayOfWeek());

        for (TimeTable item : classSchedules) {
            if (!Objects.equals(item.getId(), currentId) && Objects.equals(item.getPeriod(), request.getPeriod())) {
                throw new AppException(ErrorCode.SCHEDULE_CONFLICT);
            }
        }

        // Kiểm tra lịch học của giáo viên
        List<TimeTable> teacherSchedules = timeTableRepository
                .findByTeacherNameAndDayOfWeek(request.getTeacherName(), request.getDayOfWeek());

        for (TimeTable item : teacherSchedules) {
            if (!Objects.equals(item.getId(), currentId) && Objects.equals(item.getPeriod(), request.getPeriod())) {
                throw new AppException(ErrorCode.TEACHER_SCHEDULE_CONFLICT);
            }
        }
    }

    @Override
    public WeeklyScheduleResponse getWeeklySchedule(String className, LocalDate weekStartDate) {
        // Lấy toàn bộ lịch học của lớp
        List<TimeTable> all = timeTableRepository.findByClassName(className);

        // Nhóm các lịch học theo ngày trong tuần
        Map<DayOfWeekEnum, List<TimeTable>> byDay = all.stream()
                .collect(Collectors.groupingBy(TimeTable::getDayOfWeek));

        // Lịch học của cả tuần
        List<DailyScheduleResponse> schedule = new ArrayList<>();

        // Duyệt qua từng ngày trong tuần để lấy lịch học
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = weekStartDate.plusDays(i);
            DayOfWeekEnum enumDay = DayOfWeekEnum.valueOf(currentDate.getDayOfWeek().name());
            String displayName = "THU " + (i + 1);

            List<TimeTableResponse> lessons = byDay.getOrDefault(enumDay, List.of())
                    .stream()
                    .sorted(Comparator.comparing(TimeTable::getPeriod)) // Sắp xếp theo tiết học
                    .map(timeTableMapper::toTimeTableResponse)
                    .collect(Collectors.toList());

            schedule.add(new DailyScheduleResponse(displayName, currentDate, lessons));
        }

        return new WeeklyScheduleResponse(className, weekStartDate, schedule);
    }

    @Override
    public List<TimeTableResponse> getByClassName(String className) {
        // Lấy lịch học theo tên lớp
        return timeTableRepository.findByClassName(className).stream()
                .map(timeTableMapper::toTimeTableResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeTableResponse> getTimeTableForCurrentUser() {
        // Lấy thông tin người dùng hiện tại từ security context
        User user = getAuthenticatedUser();

        if (user.getRoles().contains(Role.ROLE_STUDENT)) {
            return getByClassName(user.getStudentClass());
        } else if (user.getRoles().contains(Role.ROLE_TEACHER)) {
            return getByTeacherName(user.getFullName());
        }

        throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    @Override
    public void delete(String id) {
        // Xóa lịch học theo ID
        TimeTable timetable = timeTableRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TIME_TABLE_NOT_FOUND));

        timeTableRepository.delete(timetable);
    }

    // ============ PRIVATE METHODS ============

    private User getAuthenticatedUser() {
        // Lấy thông tin người dùng hiện tại
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private List<TimeTableResponse> getByTeacherName(String teacherName) {
        // Lấy lịch học theo tên giáo viên
        return timeTableRepository.findByTeacherName(teacherName).stream()
                .map(timeTableMapper::toTimeTableResponse)
                .collect(Collectors.toList());
    }
}

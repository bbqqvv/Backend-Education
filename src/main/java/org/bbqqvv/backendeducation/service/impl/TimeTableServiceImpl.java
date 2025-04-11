package org.bbqqvv.backendeducation.service.impl;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.config.jwt.SecurityUtils;
import org.bbqqvv.backendeducation.dto.request.TimeTableRequest;
import org.bbqqvv.backendeducation.dto.response.DailyScheduleResponse;
import org.bbqqvv.backendeducation.dto.response.TimeTableResponse;
import org.bbqqvv.backendeducation.dto.response.WeeklyScheduleResponse;
import org.bbqqvv.backendeducation.entity.Role;
import org.bbqqvv.backendeducation.entity.TimeTable;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.exception.AppException;
import org.bbqqvv.backendeducation.exception.ErrorCode;
import org.bbqqvv.backendeducation.mapper.TimeTableMapper;
import org.bbqqvv.backendeducation.repository.TimeTableRepository;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.service.TimeTableService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
        // Kiểm tra trùng tiết
        List<TimeTable> existing = timeTableRepository.findByClassNameAndDayOfWeek(
                request.getClassName(), request.getDayOfWeek());

        for (TimeTable item : existing) {
            if (Objects.equals(item.getPeriod(), request.getPeriod())) {
                throw new AppException(ErrorCode.SCHEDULE_CONFLICT);
            }
        }

        TimeTable newEntry = TimeTable.builder()
                .className(request.getClassName())
                .subject(request.getSubject())
                .teacherName(request.getTeacherName())
                .dayOfWeek(request.getDayOfWeek())
                .period(request.getPeriod())
                .build();

        return timeTableMapper.toTimeTableResponse(timeTableRepository.save(newEntry));
    }

    @Override
    public TimeTableResponse update(String id, TimeTableRequest request) {
        TimeTable existing = timeTableRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TIME_TABLE_NOT_FOUND));

        List<TimeTable> schedules = timeTableRepository.findByClassNameAndDayOfWeek(
                request.getClassName(), request.getDayOfWeek());

        for (TimeTable item : schedules) {
            if (!item.getId().equals(id) && Objects.equals(item.getPeriod(), request.getPeriod())) {
                throw new AppException(ErrorCode.SCHEDULE_CONFLICT);
            }
        }

        existing = existing.toBuilder()
                .className(request.getClassName())
                .subject(request.getSubject())
                .teacherName(request.getTeacherName())
                .dayOfWeek(request.getDayOfWeek())
                .period(request.getPeriod())
                .build();

        return timeTableMapper.toTimeTableResponse(timeTableRepository.save(existing));
    }

    @Override
    public WeeklyScheduleResponse getWeeklySchedule(String className, LocalDate weekStartDate) {
        List<TimeTable> all = timeTableRepository.findByClassName(className);

        Map<String, List<TimeTable>> byDay = all.stream()
                .collect(Collectors.groupingBy(TimeTable::getDayOfWeek));

        List<DailyScheduleResponse> schedule = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = weekStartDate.plusDays(i);
            String dayOfWeekDisplay = "THU " + (i + 1);
            String javaDayName = currentDate.getDayOfWeek().name(); // "MONDAY"...

            List<TimeTableResponse> lessons = byDay.getOrDefault(javaDayName, List.of())
                    .stream()
                    .map(t -> new TimeTableResponse(
                            t.getId(),
                            t.getSubject(),
                            t.getTeacherName(),
                            t.getDayOfWeek(),
                            t.getPeriod()
                    ))
                    .collect(Collectors.toList());

            schedule.add(new DailyScheduleResponse(dayOfWeekDisplay, currentDate, lessons));
        }

        return new WeeklyScheduleResponse(className, weekStartDate, schedule);
    }


    @Override
    public List<TimeTableResponse> getByClassName(String className) {
        return timeTableRepository.findByClassName(className).stream()
                .map(timeTableMapper::toTimeTableResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeTableResponse> getTimeTableForCurrentUser() {
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
        TimeTable timetable = timeTableRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TIME_TABLE_NOT_FOUND));

        timeTableRepository.delete(timetable);
    }

    // ============ PRIVATE METHODS ============

    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private List<TimeTableResponse> getByTeacherName(String teacherName) {
        return timeTableRepository.findByTeacherName(teacherName).stream()
                .map(timeTableMapper::toTimeTableResponse)
                .collect(Collectors.toList());
    }
}

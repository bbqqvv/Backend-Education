package org.bbqqvv.backendeducation.service.impl;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.config.jwt.SecurityUtils;
import org.bbqqvv.backendeducation.dto.request.TimeTableRequest;
import org.bbqqvv.backendeducation.dto.response.TimeTableResponse;
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

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTableServiceImpl implements TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final UserRepository userRepository;
    private final TimeTableMapper timeTableMapper;

    @Override
    public TimeTableResponse create(TimeTableRequest request) {
        validateNoConflict(request);

        TimeTable newEntry = TimeTable.builder()
                .className(request.getClassName())
                .subject(request.getSubject())
                .teacherName(request.getTeacherName())
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
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
            if (!item.getId().equals(id) &&
                    isTimeOverlap(item.getStartTime(), item.getEndTime(),
                            request.getStartTime(), request.getEndTime())) {
                throw new AppException(ErrorCode.SCHEDULE_CONFLICT);
            }
        }

        existing = existing.toBuilder()
                .className(request.getClassName())
                .subject(request.getSubject())
                .teacherName(request.getTeacherName())
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        return timeTableMapper.toTimeTableResponse(timeTableRepository.save(existing));
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

    // ================= PRIVATE METHODS =================

    private void validateNoConflict(TimeTableRequest request) {
        List<TimeTable> existing = timeTableRepository.findByClassNameAndDayOfWeek(
                request.getClassName(), request.getDayOfWeek());

        for (TimeTable item : existing) {
            if (isTimeOverlap(item.getStartTime(), item.getEndTime(),
                    request.getStartTime(), request.getEndTime())) {
                throw new AppException(ErrorCode.SCHEDULE_CONFLICT);
            }
        }
    }

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

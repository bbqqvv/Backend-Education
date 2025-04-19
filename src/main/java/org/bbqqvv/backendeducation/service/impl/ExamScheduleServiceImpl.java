package org.bbqqvv.backendeducation.service.impl;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.config.jwt.SecurityUtils;
import org.bbqqvv.backendeducation.dto.request.ExamScheduleRequest;
import org.bbqqvv.backendeducation.dto.response.ExamScheduleResponse;
import org.bbqqvv.backendeducation.entity.ExamSchedule;
import org.bbqqvv.backendeducation.entity.Role;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.exception.AppException;
import org.bbqqvv.backendeducation.exception.ErrorCode;
import org.bbqqvv.backendeducation.mapper.ExamScheduleMapper;
import org.bbqqvv.backendeducation.repository.ExamScheduleRepository;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.service.ExamScheduleService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamScheduleServiceImpl implements ExamScheduleService {

    private final ExamScheduleRepository examScheduleRepository;
    private final ExamScheduleMapper examScheduleMapper;
    private final UserRepository userRepository;

    @Override
    public ExamScheduleResponse create(ExamScheduleRequest request) {
        validateNoConflict(request, null);

        ExamSchedule entity = examScheduleMapper.toExamSchedule(request);
        ExamSchedule saved = examScheduleRepository.save(entity);
        return examScheduleMapper.toExamScheduleResponse(saved);
    }

    @Override
    public ExamScheduleResponse update(String id, ExamScheduleRequest request) {
        ExamSchedule existing = examScheduleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SCHEDULE_NOT_FOUND));

        validateNoConflict(request, id);

        existing.setClassId(request.getClassId());
        existing.setClassName(request.getClassName());
        existing.setSubject(request.getSubject());
        existing.setExamDate(request.getExamDate());
        existing.setStartTime(request.getStartTime());
        existing.setEndTime(request.getEndTime());
        existing.setExamRoom(request.getExamRoom());
        existing.setTeacherId(request.getTeacherId());
        existing.setTeacherName(request.getTeacherName());

        ExamSchedule saved = examScheduleRepository.save(existing);
        return examScheduleMapper.toExamScheduleResponse(saved);
    }

    @Override
    public void delete(String id) {
        ExamSchedule examSchedule = examScheduleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_SCHEDULE_NOT_FOUND));
        examScheduleRepository.delete(examSchedule);
    }

    @Override
    public List<ExamScheduleResponse> getMyExamSchedule() {
        User user = getAuthenticatedUser();

        if (user.getRoles().contains(Role.ROLE_STUDENT)) {
            return examScheduleRepository.findByClassName(user.getStudentClass()).stream()  // Sử dụng studentClass
                    .map(examScheduleMapper::toExamScheduleResponse)
                    .collect(Collectors.toList());
        } else if (user.getRoles().contains(Role.ROLE_TEACHER)) {
            return examScheduleRepository.findByTeacherId(user.getId()).stream()
                    .map(examScheduleMapper::toExamScheduleResponse)
                    .collect(Collectors.toList());
        }

        throw new AppException(ErrorCode.UNAUTHORIZED);
    }


    @Override
    public List<ExamScheduleResponse> getByClassName(String className) {
        return examScheduleRepository.findByClassName(className).stream()
                .map(examScheduleMapper::toExamScheduleResponse)
                .collect(Collectors.toList());
    }

    // ================= PRIVATE =================

    private void validateNoConflict(ExamScheduleRequest request, String excludeId) {
        // Trùng lịch thi học sinh (theo studentClass)
        List<ExamSchedule> sameClassExams = examScheduleRepository.findByClassNameAndExamDate(
                request.getClassName(), request.getExamDate()); // Sử dụng className thay vì classId

        for (ExamSchedule exam : sameClassExams) {
            if (!exam.getId().equals(excludeId) &&
                    isTimeOverlap(exam.getStartTime(), exam.getEndTime(),
                            request.getStartTime(), request.getEndTime())) {
                throw new AppException(ErrorCode.SCHEDULE_CONFLICT);
            }
        }

        // Trùng lịch coi thi giáo viên (theo teacherId)
        List<ExamSchedule> sameTeacherExams = examScheduleRepository.findByTeacherIdAndExamDate(
                request.getTeacherId(), request.getExamDate());

        for (ExamSchedule exam : sameTeacherExams) {
            if (!exam.getId().equals(excludeId) &&
                    isTimeOverlap(exam.getStartTime(), exam.getEndTime(),
                            request.getStartTime(), request.getEndTime())) {
                throw new AppException(ErrorCode.TEACHER_SCHEDULE_CONFLICT);
            }
        }
    }

    private boolean isTimeOverlap(LocalTime start1, LocalTime end1,
                                  LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}

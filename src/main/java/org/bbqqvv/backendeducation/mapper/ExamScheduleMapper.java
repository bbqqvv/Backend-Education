package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.request.ExamScheduleRequest;
import org.bbqqvv.backendeducation.dto.response.ExamScheduleResponse;
import org.bbqqvv.backendeducation.dto.response.ExamScheduleResponse.ClassInfo;
import org.bbqqvv.backendeducation.dto.response.ExamScheduleResponse.TeacherInfo;
import org.bbqqvv.backendeducation.entity.ExamSchedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExamScheduleMapper {

    ExamSchedule toExamSchedule(ExamScheduleRequest examScheduleRequest);

    default ExamScheduleResponse toExamScheduleResponse(ExamSchedule exam) {
        return ExamScheduleResponse.builder()
                .id(exam.getId())
                .subject(exam.getSubject())
                .examDate(exam.getExamDate())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .examRoom(exam.getExamRoom())
                .classInfo(ClassInfo.builder()
                        .name(exam.getClassName())
                        .build())
                .teacher(TeacherInfo.builder()
                        .id(exam.getTeacherId())
                        .name(exam.getTeacherName())
                        .build())
                .build();
    }
}

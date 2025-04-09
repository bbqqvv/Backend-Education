package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.request.ExamScheduleRequest;
import org.bbqqvv.backendeducation.dto.response.ExamScheduleResponse;
import org.bbqqvv.backendeducation.entity.ExamSchedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExamScheduleMapper {
    ExamSchedule toExamSchedule(ExamScheduleRequest examScheduleRequest);
    ExamScheduleResponse toExamScheduleResponse(ExamSchedule examSchedule);
}

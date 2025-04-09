package org.bbqqvv.backendeducation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ExamScheduleResponse {
    private String id;
    private String className;
    private String subject;
    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String examRoom;
    private String teacherName;
}
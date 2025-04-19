package org.bbqqvv.backendeducation.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
// ExamScheduleRequest.java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamScheduleRequest {
    private String subject;
    private LocalDate examDate;

    private String classId;
    private String className;

    private LocalTime startTime;
    private LocalTime endTime;
    private String examRoom;

    private String teacherId;
    private String teacherName;
}

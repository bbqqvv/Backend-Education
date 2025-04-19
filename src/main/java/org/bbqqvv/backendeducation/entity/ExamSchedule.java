package org.bbqqvv.backendeducation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "exam_schedule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamSchedule {

    @Id
    private String id;

    private String classId;
    private String className;

    private String subject;

    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String examRoom;

    private String teacherId;
    private String teacherName;
}

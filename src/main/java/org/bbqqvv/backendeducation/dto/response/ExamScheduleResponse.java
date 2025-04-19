package org.bbqqvv.backendeducation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ExamScheduleResponse {
    private String id;
    private ClassInfo classInfo;
    private String subject;
    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String examRoom;
    private TeacherInfo teacher;

    @Data
    @Builder
    public static class ClassInfo {
        private String name;
    }

    @Data
    @Builder
    public static class TeacherInfo {
        private String id;
        private String name;
    }
}

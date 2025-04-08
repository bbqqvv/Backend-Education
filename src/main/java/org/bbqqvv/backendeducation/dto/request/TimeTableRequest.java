package org.bbqqvv.backendeducation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableRequest {
    private String className;
    private String subject;
    private String teacherName;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
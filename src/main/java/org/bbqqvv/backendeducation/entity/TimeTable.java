// TimeTable.java
package org.bbqqvv.backendeducation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Document(collection = "timetables")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TimeTable {
    @Id
    private String id;
    private String className;
    private String subject;
    private String teacherName;
    private DayOfWeekEnum dayOfWeek;
    private Integer period;
}
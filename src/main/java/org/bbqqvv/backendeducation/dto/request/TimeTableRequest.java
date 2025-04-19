package org.bbqqvv.backendeducation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bbqqvv.backendeducation.entity.DayOfWeekEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableRequest {
    private String className;
    private String subject;
    private String teacherName;
    private DayOfWeekEnum dayOfWeek;
    private Integer period;
}

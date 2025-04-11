package org.bbqqvv.backendeducation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyScheduleResponse {
    private String dayOfWeek;
    private LocalDate date;
    private List<TimeTableResponse> lessons;
}
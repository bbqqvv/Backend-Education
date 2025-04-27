package org.bbqqvv.backendeducation.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private String id;
    private String className;
    private String studentId;
    private String studentName;
    private String status;
    private LocalDateTime date;
    private LocalDateTime createdDate;

}

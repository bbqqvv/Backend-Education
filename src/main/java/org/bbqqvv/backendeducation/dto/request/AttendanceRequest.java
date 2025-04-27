package org.bbqqvv.backendeducation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bbqqvv.backendeducation.entity.RecordStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {
    private String userCode;
    private RecordStatus status;
    private LocalDateTime date;
}

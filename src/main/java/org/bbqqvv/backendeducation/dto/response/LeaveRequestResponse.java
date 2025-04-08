package org.bbqqvv.backendeducation.dto.response;

import lombok.*;
import org.bbqqvv.backendeducation.entity.LeaveStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestResponse {
    private String id;
    private String senderName;
    private String recipient;
    private String reason;
    private String className;
    private String imageFile;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LeaveStatus status;
    private LocalDateTime createdAt;
}

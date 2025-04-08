package org.bbqqvv.backendeducation.entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "leave_requests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequest {
    @Id
    private String id;

    private String senderName;
    private String recipient; // "HIỆU TRƯỞNG"
    private String reason;
    private String className;
    private String imageFile;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String rejectionReason;
    private LeaveStatus status; // PENDING, APPROVED, REJECTED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

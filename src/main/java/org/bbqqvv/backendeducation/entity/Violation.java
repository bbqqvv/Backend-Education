package org.bbqqvv.backendeducation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "violations") // <== Tên collection trong MongoDB
public class Violation {

    @Id
    private String id;
    private String userCode;
    private String violationCode;
    private String fullName;
    private UserType role;
    private String description;
    private ViolationLevel level;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
}

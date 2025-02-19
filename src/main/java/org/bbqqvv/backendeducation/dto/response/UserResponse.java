package org.bbqqvv.backendeducation.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String email;
    private String password;
    private String fullName;
    private String studentId;
    private String studentClass;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

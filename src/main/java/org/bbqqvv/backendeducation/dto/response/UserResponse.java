package org.bbqqvv.backendeducation.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String email;
    private String password;
    private String fullName;
    private String userCode;
    private String studentClass;
    private Set<String> teachingClasses;
    private String role;
    private UserProfileResponse profile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

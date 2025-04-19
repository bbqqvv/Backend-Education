package org.bbqqvv.backendeducation.dto.request;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    private String fullName;
    private String email;
    private String password;
    private String userCode;
    private String studentClass;
    private Set<String> teachingClasses;
    private String role;
}

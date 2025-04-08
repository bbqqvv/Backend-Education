package org.bbqqvv.backendeducation.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    private String fullName;
    private String email;
    private String password;
    private String studentCode;
    private String studentClass;
    private String role;
}

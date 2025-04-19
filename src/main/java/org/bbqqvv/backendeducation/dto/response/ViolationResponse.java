package org.bbqqvv.backendeducation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bbqqvv.backendeducation.entity.UserType;
import org.bbqqvv.backendeducation.entity.ViolationLevel;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViolationResponse {
    private String id;
    private String userCode;
    private String violationCode;
    private String fullName;
    private UserType role;
    private String description;
    private ViolationLevel level;
    private LocalDateTime createdAt;
    private String createdBy;
}

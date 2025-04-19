package org.bbqqvv.backendeducation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bbqqvv.backendeducation.entity.UserType;
import org.bbqqvv.backendeducation.entity.ViolationLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViolationRequest {
    private String userCode;
    private UserType role;
    private String description;
    private ViolationLevel level;
}

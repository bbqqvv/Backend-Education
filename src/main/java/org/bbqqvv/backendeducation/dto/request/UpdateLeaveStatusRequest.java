// dto/request/UpdateLeaveStatusRequest.java
package org.bbqqvv.backendeducation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bbqqvv.backendeducation.entity.LeaveStatus;

@Data
public class UpdateLeaveStatusRequest {
    @NotNull
    private LeaveStatus status;

    private String rejectionReason;
}

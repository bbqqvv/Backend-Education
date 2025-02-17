package org.bbqqvv.backendeducation.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    private String newPassword;
}

package org.bausit.admin.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePasswordRequest {
    @NotBlank(message = "currentPassword required")
    private String currentPassword;

    @NotBlank(message = "newPassword required")
    private String newPassword;

    private String newPasswordConfirm;

}

package com.uber.app.team23.AirRide.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UpdatePasswordDTO {
    @NotNull @NotEmpty @Size(min = 3, max = 20)
    private String newPassword;
    @NotNull @NotEmpty
    private String oldPassword;
}

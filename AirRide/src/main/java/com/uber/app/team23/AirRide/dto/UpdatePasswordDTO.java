package com.uber.app.team23.AirRide.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UpdatePasswordDTO {
    private String new_password;
    private String old_password;
}

package com.uber.app.team23.AirRide.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UpdateLocationDTO {
    private String licenseNumber;
    private double latitude;
    private double longitude;
}

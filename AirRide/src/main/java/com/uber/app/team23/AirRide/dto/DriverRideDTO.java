package com.uber.app.team23.AirRide.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DriverRideDTO {
    private int id;
    private String email;

    public DriverRideDTO(int id, String email) {
        this.id = id;
        this.email = email;
    }
}

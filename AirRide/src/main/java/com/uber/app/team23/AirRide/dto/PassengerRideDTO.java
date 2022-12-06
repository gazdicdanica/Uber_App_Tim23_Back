package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.Passenger;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PassengerRideDTO {
    private int id;
    private String email;

    public PassengerRideDTO(Passenger p){
        this.id = p.getId().intValue();
        this.email = p.getEmail();
    }

    public PassengerRideDTO(int id, String email){
        this.id = id;
        this.email = email;
    }
}

package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class UserRideDTO {
    private int id;
    private String email;

    public UserRideDTO(User user){
        this.id = user.getId().intValue();
        this.email = user.getEmail();
    }
}

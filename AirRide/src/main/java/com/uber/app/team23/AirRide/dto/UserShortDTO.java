package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import lombok.*;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserShortDTO {
    private int id;
    private String email;

    public UserShortDTO(User user){
        this.id = user.getId().intValue();
        this.email = user.getEmail();
    }
    public UserShortDTO(Passenger passenger) {
        this.id = passenger.getId().intValue();
        this.email = passenger.getEmail();
    }
}

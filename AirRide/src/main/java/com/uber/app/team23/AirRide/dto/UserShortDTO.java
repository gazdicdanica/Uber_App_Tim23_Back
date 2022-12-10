package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class UserShortDTO {
    private int id;
    private String email;

    public UserShortDTO(User user){
        this.id = user.getId().intValue();
        this.email = user.getEmail();
    }
}
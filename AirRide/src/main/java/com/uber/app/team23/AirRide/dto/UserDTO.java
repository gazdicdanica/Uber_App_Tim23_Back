package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserDTO {
    private int id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public UserDTO(User user){
        this(user.getId().intValue(), user.getName(), user.getLastName(), user.getProfilePhoto(),
                user.getPhoneNumber(), user.getEmail(), user.getAddress());
    }
}

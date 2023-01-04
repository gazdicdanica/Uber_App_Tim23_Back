package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public UserDTO(User user){
        this(user.getId(), user.getName(), user.getSurname(), user.getProfilePicture(),
                user.getTelephoneNumber(), user.getEmail(), user.getAddress());
    }

    public UserDTO(Long id, Driver driver) {
        this(id, driver.getName(), driver.getSurname(), driver.getProfilePicture(),
                driver.getTelephoneNumber(), driver.getEmail(), driver.getAddress());
    }
}

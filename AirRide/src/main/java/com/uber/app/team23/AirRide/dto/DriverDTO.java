package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class DriverDTO {
    private long id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public DriverDTO(Driver driver){
        this(driver.getId(), driver.getName(), driver.getLastName(), driver.getProfilePhoto(), driver.getPhoneNumber(),
                driver.getEmail(), driver.getAddress());
    }
    public DriverDTO(long id, String name, String surname, String profilePicture, String telephoneNumber, String email,
                     String address) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
    }
}

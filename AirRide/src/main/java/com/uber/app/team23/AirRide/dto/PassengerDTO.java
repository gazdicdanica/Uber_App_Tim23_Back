package com.uber.app.team23.AirRide.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uber.app.team23.AirRide.model.users.Passenger;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PassengerDTO {
    private int id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public PassengerDTO(Passenger passenger){
        this(passenger.getId().intValue(), passenger.getName(), passenger.getLastName(), passenger.getProfilePhoto(),
                passenger.getPhoneNumber(), passenger.getEmail(), passenger.getAddress());
    }
    public PassengerDTO(int id, String name, String lastName, String profilePhoto,
                        String phoneNumber, String email, String address) {
        this.id = id;
        this.name = name;
        this.surname = lastName;
        this.profilePicture = profilePhoto;
        this.telephoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }
}

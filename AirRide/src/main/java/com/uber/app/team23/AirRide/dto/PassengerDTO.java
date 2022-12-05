package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.Passenger;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PassengerDTO {
    private int id;
    private String name;
    private String lastName;
    private byte[] profilePhoto;
    private String phoneNumber;
    private String email;
    private String address;
    private String password;

    public PassengerDTO(Passenger passenger){
        this(passenger.getId().intValue(), passenger.getName(), passenger.getLastName(), passenger.getProfilePhoto(),
                passenger.getPhoneNumber(), passenger.getEmail(), passenger.getAddress(), passenger.getPassword());
    }
    public PassengerDTO(int id, String name, String lastName, byte[] profilePhoto,
                        String phoneNumber, String email, String address, String password) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.profilePhoto = profilePhoto;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
    }
}

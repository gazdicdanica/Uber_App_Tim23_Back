package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.Passenger;

public class PassengerDTO {
    private String name;
    private String lastName;
    private String profilePhoto;
    private String phoneNumber;
    private String email;
    private String address;
    private String password;

    public PassengerDTO(Passenger passenger){
        this(passenger.getName(), passenger.getLastName(), passenger.getProfilePhoto(),
                passenger.getPhoneNumber(), passenger.getEmail(), passenger.getAddress(), passenger.getPassword());
    }
    public PassengerDTO(String name, String lastName, String profilePhoto,
                        String phoneNumber, String email, String address, String password) {
        this.name = name;
        this.lastName = lastName;
        this.profilePhoto = profilePhoto;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
    }
}

package com.uber.app.team23.AirRide.model.users;

import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
//import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
@NoArgsConstructor
//@Entity
//@Table(name = "Passengers")
public class Passenger extends User{
//    @OneToMany(mappedBy = "passenger", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public ArrayList<Ride> rides = new ArrayList<Ride>();
//    @OneToMany(mappedBy = "passenger", fetch = FetchType.LAZY, cascade = CascadeType.ALL)

    public ArrayList<Location> favouriteLocations = new ArrayList<Location>();

    public Passenger(Long id, String name, String lastName, byte[] profilePhoto, String phoneNumber, String email,
                     String address, String password, boolean blocked, boolean active, ArrayList<Ride> rides,
                     ArrayList<Location> favouriteLocations) {
        super(id, name, lastName, profilePhoto, phoneNumber, email, address, password, blocked, active);
        this.rides = rides;
        this.favouriteLocations = favouriteLocations;
    }
}

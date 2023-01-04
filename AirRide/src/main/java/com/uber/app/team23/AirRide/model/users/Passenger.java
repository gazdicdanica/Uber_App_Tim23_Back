package com.uber.app.team23.AirRide.model.users;

import com.uber.app.team23.AirRide.model.rideData.Favorite;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import jakarta.persistence.*;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter @Setter @NoArgsConstructor
@Entity @AllArgsConstructor
@DiscriminatorValue("passenger")
public class Passenger extends User{
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ride_passengers", joinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"))
    public Set<Ride> rides = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "favorites_passenger", joinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "favorite_id", referencedColumnName = "id"))
    public Set<Favorite> favouriteLocations = new HashSet<>();

//    public Passenger(Long id, String name, String lastName, String profilePhoto, String phoneNumber, String email,
//                     String address, String password, boolean blocked, boolean active, Set<Ride> rides,
//                     Set<Location> favouriteLocations, List<Role> role, String jwt) {
//        super(id,name, lastName, profilePhoto, phoneNumber, email, address, password, blocked, active, jwt, role);
//        this.rides = rides;
//        this.favouriteLocations = favouriteLocations;
//    }
}


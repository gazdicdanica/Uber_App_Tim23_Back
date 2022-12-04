package com.uber.app.team23.AirRide.model.users.driverData;

import com.uber.app.team23.AirRide.model.rideData.Ride;

import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Document;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
//import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


//@Entity
@Getter
@Setter
@NoArgsConstructor
public class Driver extends User {
//    @OneToOne(fetch = FetchType.LAZY)
    public Document driverLicence;
//    @OneToOne(fetch = FetchType.LAZY)
    public Document registrationCertificate;
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Ride> rides = new HashSet<Ride>();
//    @OneToOne(fetch = FetchType.LAZY)
    public Vehicle vehicle;

    public Driver(Long id, String name, String lastName, byte[] profilePhoto, String phoneNumber, String email,
                  String address, String password, boolean blocked, boolean active, Document driverLicence,
                  Document registrationCertificate, Set<Ride> rides, Vehicle vehicle) {
        super(id, name, lastName, profilePhoto, phoneNumber, email, address, password, blocked, active);

        this.driverLicence = driverLicence;
        this.registrationCertificate = registrationCertificate;
        this.rides = rides;
        this.vehicle = vehicle;
    }

}


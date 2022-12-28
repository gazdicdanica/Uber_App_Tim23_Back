package com.uber.app.team23.AirRide.model.users.driverData;

import com.uber.app.team23.AirRide.model.rideData.Ride;

import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Document;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@DiscriminatorValue("driver")
@AllArgsConstructor
@Getter @Setter @NoArgsConstructor
public class Driver extends User {

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Document> documents  = new HashSet<>();

    //TODO Razmisliti da li treba ostaviti posebnu tabelu koja povezuje Ride i Driver ili ostaviti DRIVER_ID u RIDES tabeli
    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Ride> rides = new HashSet<>();
    @OneToOne(mappedBy = "driver", fetch = FetchType.LAZY)
    public Vehicle vehicle;

//    public Driver(Long id, String name, String lastName, String profilePhoto, String phoneNumber, String email,
//                  String address, String password, boolean blocked, boolean active, Set<Document> documents,
//                  Set<Ride> rides, Vehicle vehicle, Role role) {
//        super(id, name, lastName, profilePhoto, phoneNumber, email, address, password, blocked, active, role);
//
//        this.documents = documents;
//        this.rides = rides;
//        this.vehicle = vehicle;
//    }

    public void addDocument(Document document){
        this.documents.add(document);
        document.setDriver(this);
    }

    public void removeDocument(Document document){
        this.documents.remove(document);
        document.setDriver(null);
    }

}




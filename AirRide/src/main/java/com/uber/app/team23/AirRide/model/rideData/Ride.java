package com.uber.app.team23.AirRide.model.rideData;

import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
//import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Table(name = "Rides")
@Getter
@Setter
@NoArgsConstructor
public class Ride {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
//    @Column(name = "start")
    public LocalDateTime start;
//    @Column(name = "end")
    public LocalDateTime end;
//    @Column(name = "totalPrice")
    public double totalPrice;
//    @OneToMany(mappedBy = "ride", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Passenger> passengers = new HashSet<>();
//    @Column(name = "timeEstimate")
    public int timeEstimate;
//    @OneToMany(mappedBy = "ride", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Review> reviews = new HashSet<>();
//    @OneToMany(mappedBy = "ride", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Route> route = new HashSet<>();
//    @Column(name = "rideStatus")
    public RideStatus rideStatus;
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Rejection rejection;
//    @Column(name = "panic")
    public boolean panic;
//    @Column(name = "babies")
    public boolean babies;
//    @Column(name = "pets")
    public boolean pets;
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Vehicle vehicle;
    private Driver driver;

    public Ride(Long id, LocalDateTime start, LocalDateTime end, double totalPrice, Set<Passenger> passengers,
                int timeEstimate, Set<Review> reviews, Set<Route> route, RideStatus rideStatus,
                Rejection rejection, boolean panic, boolean babies, boolean pets, Vehicle vehicle, Driver driver) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.totalPrice = totalPrice;
        this.passengers = passengers;
        this.timeEstimate = timeEstimate;
        this.reviews = reviews;
        this.route = route;
        this.rideStatus = rideStatus;
        this.rejection = rejection;
        this.panic = panic;
        this.babies = babies;
        this.pets = pets;
        this.vehicle = vehicle;
        this.driver = driver;
    }
}

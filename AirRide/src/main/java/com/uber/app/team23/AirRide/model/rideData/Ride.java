package com.uber.app.team23.AirRide.model.rideData;

import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "rides")
public class Ride {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "startTime")
    private LocalDateTime start;
    @Column(name = "endTime")
    private LocalDateTime end;
    @Column(name = "totalPrice")
    private double totalPrice;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ride_passengers", joinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"))
    public Set<Passenger> passengers = new HashSet<>();
    @Column(name = "timeEstimate")
    private int timeEstimate;
    @OneToMany(mappedBy = "ride", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Review> reviews = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Route> route = new HashSet<>();
    @Column(name = "rideStatus")
    public RideStatus rideStatus;
    @OneToOne(mappedBy = "ride", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Rejection rejection;
    @Column(name = "panic")
    private boolean panic;
    @Column(name = "babies")
    private boolean babies;
    @Column(name = "pets")
    private boolean pets;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    public Vehicle vehicle;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    public void addReview(Review review){
        this.reviews.add(review);
        review.setRide(this);
    }

    public void removeReview(Review review){
        this.reviews.remove(review);
        review.setRide(null);
    }
}
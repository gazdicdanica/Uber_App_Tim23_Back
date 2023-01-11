package com.uber.app.team23.AirRide.model.rideData;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
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
    @Column(name = "id")
    private Long id;
    @Column(name = "start_time") @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @Column(name = "end_time") @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @Column(name = "total_price")
    private double totalCost;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ride_passengers", joinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"))
    public Set<Passenger> passengers = new HashSet<>();
    @Column(name = "timeEstimate")
    private int estimatedTimeInMinutes;
    @OneToMany(mappedBy = "ride", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Review> reviews = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Route> locations = new HashSet<>();
    @Column(name = "ride_status")
    public RideStatus rideStatus;
    @OneToOne(mappedBy = "ride", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Rejection rejection;
    @Column(name = "panic")
    private boolean panic;
    @Column(name = "babies")
    private boolean babyTransport;
    @Column(name = "pets")
    private boolean petTransport;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    public Vehicle vehicle;
    @Column(name = "vehicle_type")
    public VehicleEnum vehicleType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;
    @Column(name = "delay_in_minutes")
    private int delayInMinutes;

    public void addReview(Review review){
        this.reviews.add(review);
        review.setRide(this);
    }

    public void removeReview(Review review){
        this.reviews.remove(review);
        review.setRide(null);
    }
}
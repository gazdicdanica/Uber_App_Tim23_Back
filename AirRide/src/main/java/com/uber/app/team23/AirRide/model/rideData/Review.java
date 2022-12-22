package com.uber.app.team23.AirRide.model.rideData;

import com.uber.app.team23.AirRide.model.users.Passenger;
import jakarta.persistence.*;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity @Table(name = "reviews")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "grade")
    public int grade;
    @Column(name = "comment")
    public String comment;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    public Ride ride;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    public Driver driver;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passenger_id", referencedColumnName = "id", nullable = false)
    public Passenger passenger;

}


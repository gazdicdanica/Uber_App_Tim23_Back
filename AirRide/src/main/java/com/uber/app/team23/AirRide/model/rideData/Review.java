package com.uber.app.team23.AirRide.model.rideData;

import com.uber.app.team23.AirRide.model.users.Passenger;
//import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Getter
@Setter
@NoArgsConstructor
//@Table(name = "Review")
public class Review {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
//    @Column(name = "grade")
    public int grade;
//    @Column(name = "comment")
    public String comment;
//    @ManyToOne(fetch = FetchType.LAZY)
    public Ride ride;
//    @ManyToOne(fetch = FetchType.LAZY)
    public Passenger passenger;

    public Review(Long id, int grade, String comment, Ride ride, Passenger passenger) {
        this.id = id;
        this.grade = grade;
        this.comment = comment;
        this.ride = ride;
        this.passenger = passenger;
    }
}
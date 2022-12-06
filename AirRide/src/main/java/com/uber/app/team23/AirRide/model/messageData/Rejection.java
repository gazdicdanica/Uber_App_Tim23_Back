package com.uber.app.team23.AirRide.model.messageData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.User;
//import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//@Entity
@Getter
@Setter
@NoArgsConstructor
//@Table(name = "rejections")
public class Rejection {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public Long id;
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    public Ride ride;
//    @Column(name = "desc")
    public String reason;
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    public User user;
//    @Column(name = "time")
    public LocalDateTime time;
}
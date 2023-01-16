package com.uber.app.team23.AirRide.model.messageData;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity
public class Panic {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id")
    public Ride currentRide;
    @Column(name = "time") @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public LocalDateTime time;
    @Column(name = "reason")
    public String reason;
}

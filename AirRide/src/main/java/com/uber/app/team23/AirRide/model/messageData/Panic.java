package com.uber.app.team23.AirRide.model.messageData;

import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class Panic {
    public Long id;
    public User user;
    public Ride currentRide;
    public LocalDateTime time;
    public String reason;
}

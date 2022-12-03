package com.uber.app.team23.AirRide.model.messageData;

import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.User;

import java.time.LocalDateTime;

public class Panic {
    public User user;
    public Ride currentRide;
    public LocalDateTime time;
    public String description;
}

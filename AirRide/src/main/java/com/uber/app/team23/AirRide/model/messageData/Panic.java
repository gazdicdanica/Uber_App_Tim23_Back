package com.uber.app.team23.AirRide.model.messageData;

import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter @AllArgsConstructor @NoArgsConstructor
public class Panic {
    public Long id;
    public User user;
    public Ride currentRide;
    public LocalDateTime time;
    public String reason;
}

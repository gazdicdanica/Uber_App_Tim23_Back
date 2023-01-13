package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.messageData.Message;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class RideMessages {

    private Ride ride;
    private List<Message> messages;
}

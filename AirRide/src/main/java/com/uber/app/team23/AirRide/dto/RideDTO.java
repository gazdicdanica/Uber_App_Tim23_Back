package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import lombok.*;

import java.util.ArrayList;

@AllArgsConstructor @NoArgsConstructor @Data
public class RideDTO {
    private ArrayList<Route> locations = new ArrayList<>();
    private ArrayList<UserShortDTO> passengers = new ArrayList<>();
    private VehicleEnum vehicleType;
    // For scheduling in advance
    private int delayInMinutes;
    private boolean babyTransport;
    private boolean petTransport;
    private double estimatedTime;
    private float estimatedPrice;
}

package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @AllArgsConstructor
public class UnregisteredRideRequestDTO {
    private List<Route> locations = new ArrayList<>();
    private VehicleEnum vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
}

package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RideDTO {
    private ArrayList<Route> locations = new ArrayList<>();
    private ArrayList<UserShortDTO> passengers = new ArrayList<>();
    private VehicleEnum vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

}

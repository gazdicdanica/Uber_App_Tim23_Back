package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class VehicleLocatingDTO {
    private Long driverId;

    private String driverEmail;
    private Vehicle vehicle;
    private RideStatus rideStatus;

}

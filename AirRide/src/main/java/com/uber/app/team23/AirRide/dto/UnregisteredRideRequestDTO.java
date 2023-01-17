package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnregisteredRideRequestDTO {
    @NotNull
    private List<Route> locations;

    @NotNull
    private VehicleEnum vehicleType;

    @NotNull
    private boolean babyTransport;

    @NotNull
    private boolean petTransport;
}

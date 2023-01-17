package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.validation.EnumValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@AllArgsConstructor @NoArgsConstructor @Data
public class RideDTO {
    @Valid
    private ArrayList<Route> locations = new ArrayList<>();

    @Valid
    private ArrayList<UserShortDTO> passengers = new ArrayList<>();

    @NotNull
    @EnumValidator(enumClazz = VehicleEnum.class, message = "Invalid vehicle type")
    private VehicleEnum vehicleType;
    // For scheduling in advance

    @Future
    private LocalDateTime scheduledTime;

    @NotNull
    private boolean babyTransport;

    @NotNull
    private boolean petTransport;
    private double estimatedTime;
    private float estimatedPrice;
}

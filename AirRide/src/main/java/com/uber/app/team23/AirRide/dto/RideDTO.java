package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.validation.EnumValidator;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Data
public class RideDTO {
    @Valid
    private ArrayList<Route> locations = new ArrayList<>();

    
    private ArrayList<UserShortDTO> passengers = new ArrayList<>();

    @NotNull
    @EnumValidator(enumClazz = VehicleEnum.class, message = "Invalid vehicle type")
    private VehicleEnum vehicleType;
    // For scheduling in advance

    @Nullable
    private LocalDateTime scheduledTime;

    @NotNull
    private boolean babyTransport;

    @NotNull
    private boolean petTransport;
    private double estimatedTime;

    public RideDTO(ArrayList<Route> locations, ArrayList<UserShortDTO> passengers, VehicleEnum vt, boolean babyTransport,
                   boolean petTransport, LocalDateTime scheduledTime) {
        this.locations = locations;
        this.passengers = passengers;
        vehicleType = vt;
        this.petTransport = petTransport;
        this.babyTransport = babyTransport;
        this.scheduledTime = scheduledTime;
    }
}

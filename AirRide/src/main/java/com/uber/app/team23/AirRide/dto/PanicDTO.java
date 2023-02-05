package com.uber.app.team23.AirRide.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter @AllArgsConstructor @NoArgsConstructor
public class PanicDTO {
    private int id;
    private UserDTO user;
    private RideResponseDTO ride;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime time;
    private String reason;

    public PanicDTO(Panic panic){
        this.id = panic.getId().intValue();
        if(panic.getUser() != null){
            this.user = new UserDTO(panic.getUser());
        }
        this.ride = new RideResponseDTO(panic.getCurrentRide());
        this.time = panic.getTime();
        this.reason = panic.getReason();
    }
}

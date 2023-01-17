package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

@Data
@NoArgsConstructor @AllArgsConstructor
public class VehicleDTO {

    private Long id;

    @NumberFormat
    private Long driverId;

    @NotNull
    private VehicleEnum vehicleType;

    @Size(min = 4, max = 20)
    @NotNull
    private String model;

    @Size(min = 7, max = 10)
    @NotNull
    private String licenseNumber;

    @Valid
    @NotNull
    private Location currentLocation;

    @NumberFormat
    @NotNull
    private int passengerSeats;

    @NotNull
    private boolean babyTransport ;

    @NotNull
    private boolean petTransport ;

//    public VehicleDTO(Long id, Long driverId, VehicleEnum vehicleType, String model, String licenseNumber,
//                      Location currentLocation, int passengerSeats, boolean babyTransport, boolean petTransport) {
//        this.id = id;
//        this.driverId = driverId;
//        this.vehicleType = vehicleType;
//        this.model = model;
//        this.licenseNumber = licenseNumber;
//        this.currentLocation = currentLocation;
//        this.passengerSeats = passengerSeats;
//        this.babyTransport = babyTransport;
//        this.petTransport = petTransport;
//    }
}

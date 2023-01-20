package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class VehicleDTO {
    private Long id;

    private Long driverId;


    private VehicleEnum vehicleType;

    @NotNull
    @Size(min = 5, max = 30)
    private String model;

    @NotNull
    @Size(min = 5, max = 10)
    private String licenseNumber;

    @Nullable
    @Valid
    private Location currentLocation;

    @NumberFormat
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

package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class VehicleDTO {
    private Long id;
    private Long driverId;
    private VehicleEnum vehicleType;
    private String model;
    private String licenseNumber;
    private Location currentLocation;
    private int passengerSeats;
    private boolean babyTransport ;
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

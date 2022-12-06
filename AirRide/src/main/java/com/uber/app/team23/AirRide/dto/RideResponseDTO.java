package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

@Getter @Setter @NoArgsConstructor
public class RideResponseDTO {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalCost;
    private UserRideDTO driverDTO;
    private int estimatedTimeInMinutes;
    private ArrayList<Location> locations;
    private ArrayList<UserRideDTO> passengers;
    private VehicleEnum vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private RideStatus status;
    private Rejection rejection;

    public RideResponseDTO(Ride ride, ArrayList<Location> locations, ArrayList<UserRideDTO> passengers){
        this(ride.getStart(), ride.getEnd(), ride.getTotalPrice(),null, ride.getTimeEstimate(),
                null, null, ride.getVehicle().getVehicleType().getType(), ride.isBabies(),
                ride.isPets(), ride.getRideStatus(), ride.getRejection());
        this.driverDTO = new UserRideDTO(ride.getDriver().getId().intValue(), ride.getDriver().getEmail());
        this.locations = locations;
        this.passengers = passengers;

    }

    public RideResponseDTO(LocalDateTime startTime, LocalDateTime endTime, double totalCost, UserRideDTO driverDTO,
                           int estimatedTimeInMinutes, ArrayList<Location> locations, ArrayList<UserRideDTO> passengers,
                           VehicleEnum vehicleType, boolean babyTransport, boolean petTransport, RideStatus status, Rejection rejection) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.driverDTO = driverDTO;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.locations = locations;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.status = status;
        this.rejection = rejection;
    }
}

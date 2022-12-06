package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

@Getter @Setter
public class RideResponseDTO {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalCost;
    private DriverRideDTO driverDTO;
    private int estimatedTimeInMinutes;
    private ArrayList<Location> locations;
    private ArrayList<PassengerRideDTO> passengers;
    private VehicleEnum vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private RideStatus status;

    public RideResponseDTO(Ride ride, ArrayList<Location> locations, ArrayList<PassengerRideDTO> passengers){
        this(ride.getStart(), ride.getEnd(), ride.getTotalPrice(),null, ride.getTimeEstimate(),
                null, null, ride.getVehicle().getVehicleType().getType(), ride.isBabies(),
                ride.isPets(), ride.getRideStatus());
        this.driverDTO = new DriverRideDTO(ride.getDriver().getId().intValue(), ride.getDriver().getEmail());
        this.locations = locations;
        this.passengers = passengers;

    }

    public RideResponseDTO(LocalDateTime startTime, LocalDateTime endTime, double totalCost, DriverRideDTO driverDTO,
                           int estimatedTimeInMinutes, ArrayList<Location> locations, ArrayList<PassengerRideDTO> passengers, VehicleEnum vehicleType, boolean babyTransport, boolean petTransport, RideStatus status) {
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
    }
}

package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter @Setter @NoArgsConstructor
public class RideResponseDTO {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalCost;
    private UserShortDTO driver;
    private int estimatedTimeInMinutes;
    private ArrayList<Route> locations;
    private ArrayList<UserShortDTO> passengers;
    private VehicleEnum vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private RideStatus status;
    private Rejection rejection;

    public RideResponseDTO(Ride ride, ArrayList<Route> locations, ArrayList<UserShortDTO> passengers){
        this(ride.getId(), ride.getStart(), ride.getEnd(), ride.getTotalPrice(),null, ride.getTimeEstimate(),
                null, null, ride.getVehicle().getVehicleType().getType(), ride.isBabies(),
                ride.isPets(), ride.getRideStatus(), ride.getRejection());
        this.driver = new UserShortDTO(ride.getDriver().getId().intValue(), ride.getDriver().getEmail());
        this.locations = locations;
        this.passengers = passengers;

    }

    public RideResponseDTO(Long id, LocalDateTime startTime, LocalDateTime endTime, double totalCost, UserShortDTO driver,
                           int estimatedTimeInMinutes, ArrayList<Route> locations, ArrayList<UserShortDTO> passengers,
                           VehicleEnum vehicleType, boolean babyTransport, boolean petTransport, RideStatus status, Rejection rejection) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.driver = driver;
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

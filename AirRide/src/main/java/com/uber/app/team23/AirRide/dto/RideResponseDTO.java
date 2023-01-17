package com.uber.app.team23.AirRide.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter @Setter @NoArgsConstructor
public class RideResponseDTO {

    private Long id;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime startTime;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
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
    private LocalDateTime scheduledTime;

    public RideResponseDTO(Ride ride){
        this.id = ride.getId();
        this.startTime = ride.getStartTime();
        this.endTime = ride.getEndTime();
        this.totalCost = ride.getTotalCost();
        if(ride.getDriver() != null){
            this.driver = new UserShortDTO(ride.getDriver());
        }
        this.estimatedTimeInMinutes = ride.getEstimatedTimeInMinutes();
        this.locations = new ArrayList<>(ride.getLocations());
        this.passengers = new ArrayList<>();
        for(User u: ride.getPassengers()){
            UserShortDTO dto = new UserShortDTO(u);
            this.passengers.add(dto);
        }
        this.vehicleType = ride.getVehicleType();
        this.babyTransport = ride.isBabyTransport();
        this.petTransport = ride.isPetTransport();
        this.status = ride.getStatus();
        this.rejection = ride.getRejection();
        this.scheduledTime = ride.getScheduledTime();
    }

}

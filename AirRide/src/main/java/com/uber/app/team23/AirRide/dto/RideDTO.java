package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;


@Getter @Setter
public class RideDTO {
    private ArrayList<Route> locations = new ArrayList<>();
    private ArrayList<UserRideDTO> passengers = new ArrayList<>();
    private VehicleEnum vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public RideDTO (Ride ride){
        locations.addAll(ride.getRoute());
        for(Passenger p : ride.getPassengers()){
            passengers.add(new UserRideDTO(p));
        }

        this.vehicleType = ride.getVehicle().getVehicleType().getType();
        this.babyTransport = ride.isBabies();
        this.petTransport = ride.isPets();

    }

    public RideDTO(ArrayList<Route> locations, ArrayList<UserRideDTO> passengers, VehicleEnum vehicleType, boolean babyTransport, boolean petTransport) {
        this.locations = locations;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }
}

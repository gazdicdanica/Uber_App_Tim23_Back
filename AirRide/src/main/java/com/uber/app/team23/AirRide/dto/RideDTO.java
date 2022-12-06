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
    private ArrayList<Location> locations = new ArrayList<>();
    private ArrayList<PassengerRideDTO> passengers = new ArrayList<>();
    private VehicleEnum vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public RideDTO (Ride ride){
        for(Route route : ride.getRoute()){
            locations.add(route.endLocation);
            locations.add(route.startLocation);
        }
        for(Passenger p : ride.getPassengers()){
            passengers.add(new PassengerRideDTO(p));
        }

        this.vehicleType = ride.getVehicle().getVehicleType().getType();
        this.babyTransport = ride.isBabies();
        this.petTransport = ride.isPets();

    }

    public RideDTO(ArrayList<Location> locations, ArrayList<PassengerRideDTO> passengers, VehicleEnum vehicleType, boolean babyTransport, boolean petTransport) {
        this.locations = locations;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }
}

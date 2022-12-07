package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter @Setter
public class RideDTO {
    private ArrayList<Route> locations = new ArrayList<>();
    private ArrayList<UserShortDTO> passengers = new ArrayList<>();
    private VehicleEnum vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public RideDTO (Ride ride){
        locations.addAll(ride.getRoute());
        for(Passenger p : ride.getPassengers()){
            passengers.add(new UserShortDTO(p));
        }

        this.vehicleType = ride.getVehicle().getVehicleType().getType();
        this.babyTransport = ride.isBabies();
        this.petTransport = ride.isPets();

    }

    public RideDTO(ArrayList<Route> locations, ArrayList<UserShortDTO> passengers, VehicleEnum vehicleType, boolean babyTransport, boolean petTransport) {
        this.locations = locations;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }
}

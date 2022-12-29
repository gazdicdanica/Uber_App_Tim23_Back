package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.RideDTO;
import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.dto.UserShortDTO;
import com.uber.app.team23.AirRide.mapper.PassengerDTOMapper;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import com.uber.app.team23.AirRide.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private PassengerService passengerService;

    public Ride save(RideDTO rideDTO){
        Ride ride = new Ride();
        ride.setStart(LocalDateTime.now());

        Set<Passenger> passengers = rideDTO.getPassengers().stream().map(PassengerDTOMapper::fromShortDTOToPassenger).collect(Collectors.toSet());
        ride.setPassengers(passengers);
        Set<Route> routes = new HashSet<>(rideDTO.getLocations());
        ride.setRoute(routes);
        ride.setRideStatus(RideStatus.PENDING);
        ride.setPanic(false);
        ride.setBabies(rideDTO.isBabyTransport());
        ride.setPets(rideDTO.isPetTransport());

        return rideRepository.save(ride);
    }
}

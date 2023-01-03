package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.RideDTO;
import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.dto.UserShortDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.HashSet;


@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private RouteService routeService;

    public Ride findOne(Long id){
        return rideRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ride does not exist"));
    }

    public RideResponseDTO findActiveByDriver(Long driverId){
        return rideRepository.findActiveByDriver(driverId).orElseThrow(() -> new EntityNotFoundException("Active ride does not exist"));
    }

    public RideResponseDTO findActiveByPassenger(Long passengerId){
        return rideRepository.findActiveByPassenger(passengerId).orElseThrow(() -> new EntityNotFoundException("Active ride does not exist"));
    }

    public Ride addPassengers(RideDTO rideDTO,Long id){
        Ride ride = this.findOne(id);
        ride.setPassengers(new HashSet<>());
        for(UserShortDTO user: rideDTO.getPassengers()){
            Passenger p = passengerService.findOne((long) user.getId());
            ride.getPassengers().add(p);
        }
        return rideRepository.save(ride);
    }

    public Ride addRoutes(RideDTO rideDTO, Long id){
        Ride ride = this.findOne(id);
        ride.setLocations(new HashSet<>());
        for(Route route: rideDTO.getLocations()){
            Route r = routeService.findByLocationAddress(route.getDeparture().getAddress(), route.getDestination().getAddress());
            if(r == null){
                r = routeService.save(route);
            }
            ride.getLocations().add(r);
        }
        return rideRepository.save(ride);
    }

    public Ride save(RideDTO rideDTO){
        Ride ride = new Ride();
        ride.setStart(LocalDateTime.now());
        ride.setRideStatus(RideStatus.PENDING);
        ride.setPanic(false);
        ride.setVehicleType(rideDTO.getVehicleType());
        ride.setBabies(rideDTO.isBabyTransport());
        ride.setPets(rideDTO.isPetTransport());
        return rideRepository.save(ride);
    }

    @Transactional
    public RideResponseDTO withdrawRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getRideStatus() == RideStatus.ACCEPTED || ride.getRideStatus() == RideStatus.PENDING){
            throw new BadRequestException("Cannot cancel a ride that is not in status PENDING or ACCEPTED");
        }
        ride.setRideStatus(RideStatus.CANCELED);
        return new RideResponseDTO(rideRepository.save(ride));
    }

    @Transactional
    public RideResponseDTO startRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getRideStatus() == RideStatus.ACCEPTED){
            ride.setRideStatus(RideStatus.ACTIVE);
            ride.setStart(LocalDateTime.now());
            return new RideResponseDTO(rideRepository.save(ride));
        }
        throw new BadRequestException("Cannot start a ride that is not in status ACCEPTED!");
    }

    @Transactional
    public RideResponseDTO acceptRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getRideStatus() != RideStatus.PENDING){
            throw new BadRequestException("Cannot accept a ride that is not in status PENDING!");
        }
        ride.setRideStatus(RideStatus.ACCEPTED);
        // TODO set driver from token
        return new RideResponseDTO(rideRepository.save(ride));
    }

    @Transactional
    public RideResponseDTO endRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getRideStatus() != RideStatus.ACTIVE){
            throw new BadRequestException("Cannot end a ride that is not in status ACTIVE!");
        }
        ride.setRideStatus(RideStatus.FINISHED);
        ride.setEnd(LocalDateTime.now());
        return new RideResponseDTO(rideRepository.save(ride));
    }

    @Transactional
    public RideResponseDTO cancelRide(Long id, Rejection rejection){
        Ride ride = this.findOne(id);
        if(ride.getRideStatus() != RideStatus.PENDING){
            throw new BadRequestException("Cannot cancel a ride that is not in status PENDING");
        }
        // Rejection repository?
        ride.setRideStatus(RideStatus.CANCELED);
        rejection.setRide(ride);
        rejection.setTime(LocalDateTime.now());
        ride.setRejection(rejection);
        return new RideResponseDTO(rideRepository.save(ride));
    }

    public Ride setPanic(Long id, Panic panic){
        Ride ride = this.findOne(id);
        ride.setPanic(true);
        // Do we have to change ride status???
        return rideRepository.save(ride);
    }
}

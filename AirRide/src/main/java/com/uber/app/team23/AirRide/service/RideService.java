package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.RideDTO;
import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.dto.UserShortDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.mapper.RideDTOMapper;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.HashSet;


@Transactional
@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private UserService userService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private RideSchedulingService rideSchedulingService;

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

    public void checkPassengerPendingRide(Long passengerId){
        Ride ride = rideRepository.findPendingByPassenger(passengerId).orElse(null);
        if(ride != null){
            throw new BadRequestException("Cannot create a ride while you have one already pending!");
        }
    }

    public Driver findPotentialDriver(Ride ride){
        // TODO send notification to driver
        return this.rideSchedulingService.findDriver(ride);
    }

    public Ride save(RideDTO rideDTO){
        for(UserShortDTO u : rideDTO.getPassengers()){
            this.checkPassengerPendingRide((long)u.getId());
        }
        Ride ride = new Ride();
        // potential start of ride
        ride.setStartTime(LocalDateTime.now().plusMinutes(rideDTO.getDelayInMinutes()));
        ride.setRideStatus(RideStatus.PENDING);
        ride.setPanic(false);
        ride.setVehicleType(rideDTO.getVehicleType());
        ride.setBabyTransport(rideDTO.isBabyTransport());
        ride.setPetTransport(rideDTO.isPetTransport());
        ride.setDelayInMinutes(rideDTO.getDelayInMinutes());
        return rideRepository.save(ride);
    }

    

    public RideResponseDTO withdrawRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getRideStatus() == RideStatus.ACCEPTED || ride.getRideStatus() == RideStatus.PENDING){
            throw new BadRequestException("Cannot cancel a ride that is not in status PENDING or ACCEPTED");
        }
        ride.setRideStatus(RideStatus.CANCELED);

        return RideDTOMapper.fromRideToDTO(rideRepository.save(ride));
    }

    public Ride addDriver(Ride ride, Driver driver){
        ride.setDriver(driver);
        return rideRepository.save(ride);
    }

    public RideResponseDTO startRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getRideStatus() == RideStatus.ACCEPTED){
            ride.setRideStatus(RideStatus.ACTIVE);
            ride.setStartTime(LocalDateTime.now());
            return RideDTOMapper.fromRideToDTO(rideRepository.save(ride));
        }
        throw new BadRequestException("Cannot start a ride that is not in status ACCEPTED!");
    }

    public RideResponseDTO acceptRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getRideStatus() != RideStatus.PENDING){
            throw new BadRequestException("Cannot accept a ride that is not in status PENDING!");
        }
        ride.setRideStatus(RideStatus.ACCEPTED);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userService.isDriver(user)){
            Driver d = (Driver) user;
            ride.setDriver(d);
        }
        return RideDTOMapper.fromRideToDTO(rideRepository.save(ride));
    }

    public RideResponseDTO endRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getRideStatus() != RideStatus.ACTIVE){
            throw new BadRequestException("Cannot end a ride that is not in status ACTIVE!");
        }
        ride.setRideStatus(RideStatus.FINISHED);
        ride.setEndTime(LocalDateTime.now());
        return RideDTOMapper.fromRideToDTO(rideRepository.save(ride));
    }

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
        return RideDTOMapper.fromRideToDTO(rideRepository.save(ride));
    }

    public Ride setPanic(Long id, Panic panic){
        Ride ride = this.findOne(id);
        ride.setPanic(true);
        // Do we have to change ride status???
        return rideRepository.save(ride);
    }

    public Page<Ride> findAllByDriver(Driver byId, Pageable pageable) {
        return rideRepository.findAllByDriver(byId, pageable);
    }
}

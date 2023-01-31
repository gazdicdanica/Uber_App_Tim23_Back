package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.mapper.RideDTOMapper;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    RideRepository rideRepository;
    @Autowired
    UserService userService;


    public List<RideResponseDTO> getRidesForDateRange(LocalDateTime start, LocalDateTime end){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Ride> rides;
        if(userService.isDriver(user)){
            rides = rideRepository.findAllByDriverAndStatusAndStartTimeBetween((Driver) user, RideStatus.FINISHED ,start, end);

        }else{
            rides = rideRepository.findAllByPassengersContainingAndStatusAndStartTimeBetween((Passenger) user,RideStatus.FINISHED ,start, end);
        }

        return rides.stream().map(RideDTOMapper::fromRideToDTO).toList();

    }
}

package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {

    Optional<Ride> findByDriverAndStatus(Driver driver, RideStatus status);
    List<Ride> findByPassengersContainingAndStatus(Passenger passenger, RideStatus status);

    Page<Ride> findAllByDriver(User byId, Pageable pageable);

    Page<Ride> findByPassengersContaining(Passenger passenger, Pageable pageable);

    List<Ride> findAllByDriver(User driver);

    List<Ride> findAllByPassengersContaining(User user);

    List<Ride> findByStatus(RideStatus status);

    List<Ride> findAllByPassengersContainingAndStatusAndStartTimeBetween(Passenger passenger, RideStatus status, LocalDateTime start, LocalDateTime end);

    List<Ride> findAllByDriverAndStatusAndStartTimeBetween(Driver driver, RideStatus status,LocalDateTime start, LocalDateTime end);
}

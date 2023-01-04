package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByEmail(String email);
}

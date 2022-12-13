package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}

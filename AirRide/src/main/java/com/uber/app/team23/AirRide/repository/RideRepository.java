package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.rideData.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {
}

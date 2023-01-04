package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.rideData.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    public Optional<Location> findByAddress(String address);
}

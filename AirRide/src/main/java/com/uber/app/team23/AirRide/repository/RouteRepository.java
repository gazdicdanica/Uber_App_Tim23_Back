package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query(value = "select r from Route r where r.departure.id=?1 and r.destination.id=?2")
    public Optional<Route> findByLocationIds(Long departureId, Long destinationId);

    @Query(value="select r from Route r where r.departure.address=?1 and r.destination.address=?2")
    public Optional<Route> findByLocationAddress(String departureAddress, String destinationAddress);
}

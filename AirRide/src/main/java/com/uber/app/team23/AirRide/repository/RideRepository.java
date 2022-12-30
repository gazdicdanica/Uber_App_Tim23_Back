package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {

    @Query(value = "select new com.uber.app.team23.AirRide.dto.RideResponseDTO(r) from Ride r left join fetch r.driver d where d.id=?1 and r.rideStatus=3")
    public Optional<RideResponseDTO> findActiveByDriver(Long driverId);

    @Query(value = "select new com.uber.app.team23.AirRide.dto.RideResponseDTO(r) from Ride r join r.passengers p where p.id=?1 and r.rideStatus=3")
    public Optional<RideResponseDTO> findActiveByPassenger(Long passengerId);
}

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

import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {

    @Query(value = "select r from Ride r join r.passengers p where p.id=?1 and r.status=0")
    Optional<Ride> findPendingByPassenger(Long passengerId);
    @Query(value = "select r from Ride r join r.passengers pass where pass.id=?1 and r.status = 1")
    Optional<Ride> findAcceptedByPassenger(Long pId);

    @Query(value = "select new com.uber.app.team23.AirRide.dto.RideResponseDTO(r) from Ride r left join fetch r.driver d where d.id=?1 and r.status=3")
    Optional<RideResponseDTO> findActiveByDriver(Long driverId);

    @Query(value = "select new com.uber.app.team23.AirRide.dto.RideResponseDTO(r) from Ride r left join fetch r.driver d where d.id=?1 and r.status=1")
    Optional<RideResponseDTO> findAcceptedByDriver(Long driverId);

    @Query(value = "select new com.uber.app.team23.AirRide.dto.RideResponseDTO(r) from Ride r join r.passengers p where p.id=?1 and r.status=3")
    Optional<RideResponseDTO> findActiveByPassenger(Long passengerId);

    Page<Ride> findAllByDriver(User byId, Pageable pageable);

    Page<Ride> findByPassengersContaining(Passenger passenger, Pageable pageable);

    List<Ride> findAllByDriver(User driver);

    List<Ride> findAllByPassengersContaining(User user);

    List<Ride> findByStatus(RideStatus status);
}

package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);

    @Query("select u.password from User u where u.id=?1")
    String findPasswordById(Long id);

    @Query(value = "select d from Driver d where d.online = true")
    List<Driver> findOnlineDrivers();
}

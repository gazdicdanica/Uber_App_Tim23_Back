package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByEmail(String email);
}

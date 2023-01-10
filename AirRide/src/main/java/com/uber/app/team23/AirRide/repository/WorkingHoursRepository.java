package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {

    @Query(value = "select wh from WorkingHours wh where wh.driver = ?1 and wh.start > ?2")
    List<WorkingHours> findByDriverInLastDay(Driver driver, LocalDateTime lastDay);
}

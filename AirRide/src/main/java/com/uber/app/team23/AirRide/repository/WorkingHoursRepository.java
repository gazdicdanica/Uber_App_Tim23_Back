package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {

    @Query(value = "select wh from WorkingHours wh where wh.driver = ?1 and wh.start > ?2")
    List<WorkingHours> findByDriverInLastDay(Driver driver, LocalDateTime lastDay);

    @Query(value = "select wh from WorkingHours wh where wh.driver = ?1 and wh.end is null")
    Optional<WorkingHours> findShiftInProgress(Driver driver);
}

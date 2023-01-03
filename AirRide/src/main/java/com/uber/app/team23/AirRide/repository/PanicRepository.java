package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.messageData.Panic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PanicRepository extends JpaRepository<Panic, Long> {
}

package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.messageData.Rejection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RejectionRepository extends JpaRepository<Rejection, Long> {
}

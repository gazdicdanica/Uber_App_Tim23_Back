package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.UserActivation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivationRepository extends JpaRepository<UserActivation, Long> {

}

package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.PasswordResetData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetDataRepository extends JpaRepository<PasswordResetData, Long> {

    Optional <PasswordResetData> findByCode(String code);
}

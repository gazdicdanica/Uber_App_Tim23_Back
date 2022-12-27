package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}

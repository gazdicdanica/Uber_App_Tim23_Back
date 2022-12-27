package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByName(String name);
}

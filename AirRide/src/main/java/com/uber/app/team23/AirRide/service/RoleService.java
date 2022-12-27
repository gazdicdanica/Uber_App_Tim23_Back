package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findById(Long id) {
        return this.roleRepository.getOne(id);
    }

    public List<Role> findByName(String name) {
        return this.roleRepository.findByName(name);
    }
}

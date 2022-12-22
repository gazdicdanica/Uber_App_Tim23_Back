package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.repository.DriverRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;

    public Page<Driver> findAll(Pageable page) {
        return driverRepository.findAll(page);
    }

    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }

    public Driver findOne(Long id) throws NullPointerException {
        return driverRepository.findById(id).orElseGet(null);
    }
}

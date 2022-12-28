package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.repository.DriverRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Driver> findAll(Pageable page) {
        return driverRepository.findAll(page);
    }

    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    public Driver save(Driver driver) throws ConstraintViolationException {

        Driver newDriver = new Driver();
        newDriver.setPassword(passwordEncoder.encode(driver.getPassword()));
        newDriver.setName(driver.getName());
        newDriver.setSurname(driver.getSurname());
        newDriver.setProfilePicture(driver.getProfilePicture());
        newDriver.setTelephoneNumber(driver.getTelephoneNumber());
        newDriver.setEmail(driver.getEmail());
        newDriver.setAddress(driver.getAddress());
        return driverRepository.save(newDriver);
    }

    public Driver findByEmail(String email) {
        return driverRepository.findByEmail(email);
    }
    public Driver findOne(Long id) throws NullPointerException {
        return driverRepository.findById(id).orElseGet(null);
    }
}

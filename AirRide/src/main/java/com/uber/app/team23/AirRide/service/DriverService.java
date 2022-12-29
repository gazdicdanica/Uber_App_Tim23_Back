package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.UserDTO;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.repository.DriverRepository;
import com.uber.app.team23.AirRide.repository.PassengerRepository;
import com.uber.app.team23.AirRide.repository.UserRepository;
import jakarta.persistence.LockModeType;
import jakarta.validation.ConstraintViolationException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Driver> findAll(Pageable page) {
        return driverRepository.findAll(page);
    }

    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    public Driver update(Driver driver) {
        return driverRepository.save(driver);
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
        return driverRepository.findById(id).orElse(null);
    }

    public Driver findById(Long id) {
        return driverRepository.findById(id).orElse(null);
    }

    public boolean driverExists(Long id) {
        Driver driver =  driverRepository.findById(id).orElse(null);
        if(driver == null) {
            return false;
        } else {
            return true;
        }
    }
    public ResponseEntity<Object> resolveResponse(Driver driver, String message) {
        if(driver == null) {
            JSONObject json = new JSONObject();
            json.put("message", message);
            return new ResponseEntity<>(json.toString(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new UserDTO(driver), HttpStatus.OK);
        }
    }

    public Driver changeDriverData(Driver driver, Driver driverDTO) {
        driver.setName(driverDTO.getName());
        driver.setSurname(driverDTO.getSurname());
        driver.setProfilePicture(driverDTO.getProfilePicture());
        driver.setTelephoneNumber(driverDTO.getTelephoneNumber());
        driver.setEmail(driverDTO.getEmail());
        driver.setAddress(driverDTO.getAddress());
        driver.setPassword(passwordEncoder.encode(driverDTO.getPassword()));

        return driver;
    }
}

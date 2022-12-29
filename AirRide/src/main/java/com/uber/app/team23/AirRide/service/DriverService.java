package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.DriverDocumentsDTO;
import com.uber.app.team23.AirRide.exceptions.EmailTakenException;
import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Document;
import com.uber.app.team23.AirRide.repository.DocumentRepository;
import com.uber.app.team23.AirRide.repository.DriverRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DriverService {

    private static final String USER_DOES_NOT_EXIST = "User With This Email Does Not Exist";
    private static final String USER_ALREADY_EXISTS = "User With This Email Already Exists";
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DocumentRepository documentRepository;

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
        Driver check = driverRepository.findByEmail(driver.getEmail()).orElse(null);
        if (check != null) {
            throw new EmailTakenException(USER_ALREADY_EXISTS);
        }

        Driver newDriver = new Driver();
        newDriver.setPassword(passwordEncoder.encode(driver.getPassword()));
        newDriver.setName(driver.getName());
        newDriver.setSurname(driver.getSurname());
        newDriver.setProfilePicture(driver.getProfilePicture());
        newDriver.setTelephoneNumber(driver.getTelephoneNumber());
        newDriver.setEmail(driver.getEmail());
        newDriver.setAddress(driver.getAddress());
        newDriver.setBlocked(false);
        newDriver.setActive(false);

        List<Role> li = new ArrayList<>();
        li.add(new Role(1L, "ROLE_DRIVER"));
        newDriver.setRole(li);
        return driverRepository.save(newDriver);
    }

    public Driver findByEmail(String email) {
        Driver driver = driverRepository.findByEmail(email).orElse(null);
        if (driver == null) {
            throw new EmailTakenException(USER_DOES_NOT_EXIST);
        }
        return driver;
    }
    public Driver findOne(Long id) throws NullPointerException {
        Driver driver = driverRepository.findById(id).orElse(null);
        if (driver == null) {
            throw new EmailTakenException(USER_DOES_NOT_EXIST);
        }
        return driver;
    }

    public Driver findById(Long id) {
        Driver driver = driverRepository.findById(id).orElse(null);
        if (driver == null) {
            throw new EmailTakenException(USER_DOES_NOT_EXIST);
        }
        return driver;
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

    public DriverDocumentsDTO getDocuments(Driver driver) {
        Document document = documentRepository.findAllByDriverId(driver.getId());

        DriverDocumentsDTO dto = new DriverDocumentsDTO(document.getId(), document.getName(), document.getPhoto(), document.getDriver().getId());
        return dto;
    }
}

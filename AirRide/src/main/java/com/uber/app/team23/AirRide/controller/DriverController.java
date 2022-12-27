package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.mapper.DriverDTOMapper;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.service.DriverService;
import jakarta.persistence.Id;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController @RequestMapping("api/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createDriver(@RequestBody UserDTO userDTO) {
        Driver driver = new Driver();
        driver.setId((long)123);
        driver.setPassword("123adq");
        driver.setName(userDTO.getName());
        driver.setLastName(userDTO.getSurname());
        driver.setProfilePhoto(userDTO.getProfilePicture());
        driver.setPhoneNumber(userDTO.getTelephoneNumber());
        driver.setEmail(userDTO.getEmail());
        driver.setAddress(userDTO.getAddress());

        driver = driverService.save(driver);
        return new ResponseEntity<>(new UserDTO(driver), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPaginatedDTO> getPaginatedDrivers(Pageable page) {
        Page<Driver> drivers = driverService.findAll(page);

        List<UserDTO> users = drivers.stream().map(DriverDTOMapper::fromDriverToDTO).collect(Collectors.toList());

        System.err.println(users.size());

        return new ResponseEntity<>(new UserPaginatedDTO(users), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getDriver(@PathVariable Long id) {
        try {
            Driver driver = driverService.findOne(id);
            if (driver != null) {
                return new ResponseEntity<>(new UserDTO(driver), HttpStatus.OK);
            }
        } catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public ResponseEntity<UserDTO> updateDriver(@RequestBody UserDTO driverDTO, @PathVariable Integer id) {
        Driver driver = new Driver();
        driver.setId((long) id);
        driver.setName(driverDTO.getName());
        driver.setLastName(driverDTO.getSurname());
        driver.setProfilePhoto(driverDTO.getProfilePicture());
        driver.setPhoneNumber(driverDTO.getTelephoneNumber());
        driver.setEmail(driverDTO.getEmail());
        driver.setAddress(driverDTO.getAddress());

        return new ResponseEntity<>(new UserDTO(driver), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/documents")
    public ResponseEntity<DriverDocumentsDTO> getDriverDocuments(@PathVariable Integer id) {
        Driver driver = new Driver();
        driver.setId((long)10);

        DriverDocumentsDTO documentsDTO = new DriverDocumentsDTO((long)123, "Vozačka dozvola",
                "U3dhZ2dlciByb2Nrcw=", driver.getId());

        return new ResponseEntity<>(documentsDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/document/{id}")
    public ResponseEntity<DriverDocumentsDTO> deleteDriverDocuments(@PathVariable Integer id) {
        DriverDocumentsDTO documentsDTO = new DriverDocumentsDTO();
        documentsDTO.setId(id);
        return new ResponseEntity<>(documentsDTO, HttpStatus.NO_CONTENT);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/documents")
    public ResponseEntity<DriverDocumentsDTO> addDriverDocuments(@RequestBody DriverDocumentsDTO documentsDTO, @PathVariable Integer id) {
        DriverDocumentsDTO driverDocumentsDTO = new DriverDocumentsDTO();
        driverDocumentsDTO.setId((long) 123);
        driverDocumentsDTO.setName(documentsDTO.getName());
        driverDocumentsDTO.setDocumentImage(documentsDTO.getDocumentImage());
        driverDocumentsDTO.setDriverId((long) id);

        return new ResponseEntity<>(driverDocumentsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> getVehicleForDriver(@PathVariable Integer id) {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setId((long) 123);
        vehicle.setDriverId((long) id);
        vehicle.setVehicleType(VehicleEnum.STANDARDNO);
        vehicle.setModel("VW Golf 2");
        vehicle.setLicenseNumber("NS 123-AB");
        vehicle.setCurrentLocation(new Location((long) 1, 19.833549, 45.267136, "Bulevar oslobodjenja 46"));
        vehicle.setPassengerSeats(4);
        vehicle.setBabyTransport(true);
        vehicle.setPetTransport(true);

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> addVehicleToDriver(@PathVariable Integer id, @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setId((long) 12);
        vehicle.setDriverId((long) id);
        vehicle.setVehicleType(vehicleDTO.getVehicleType());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setLicenseNumber(vehicleDTO.getLicenseNumber());
        vehicle.setCurrentLocation(new Location((long) 1, 19.833549, 45.267136, "Bulevar oslobodjenja 46"));
        vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());
        vehicle.setBabyTransport(vehicleDTO.isBabyTransport());
        vehicle.setPetTransport(vehicleDTO.isPetTransport());

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> updateDriverVehicle(@PathVariable Integer id, @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setId((long) 12);
        vehicle.setDriverId((long) id);
        vehicle.setVehicleType(vehicleDTO.getVehicleType());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setLicenseNumber(vehicleDTO.getLicenseNumber());
        vehicle.setCurrentLocation(new Location((long) 1, 19.833549, 45.267136, "Bulevar oslobodjenja 46"));
        vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());
        vehicle.setBabyTransport(vehicleDTO.isBabyTransport());
        vehicle.setPetTransport(vehicleDTO.isPetTransport());

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/working-hour")
    public ResponseEntity<DriverWorkingHoursDTO> getTotalWorkHours(
            @PathVariable Integer id, @RequestParam int page, @RequestParam int size, @RequestParam String from,
            @RequestParam String to) {

        Driver d = new Driver();
        WorkingHours wh1 = new WorkingHours(LocalDateTime.now(), LocalDateTime.now().plusHours(2), d, (long) 1);
        WorkingHours wh2 = new WorkingHours(LocalDateTime.now(), LocalDateTime.now().plusHours(2),d, (long) 1);
        DriverWorkingHoursDTO workingHoursDTO = new DriverWorkingHoursDTO();
        workingHoursDTO.updateWorkingHours(wh1);
        workingHoursDTO.updateWorkingHours(wh2);

        return new ResponseEntity<>(workingHoursDTO, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/working-hour")
    public ResponseEntity<WorkingHours> updateDriverWorkingHours(@PathVariable Integer id) {
        // Time generated when driver logged
        LocalDateTime startShift = LocalDateTime.now().minusHours(3);
        // Time generated when driver finished shift
        LocalDateTime endShift = LocalDateTime.now();
        Driver d = new Driver();
        WorkingHours wh = new WorkingHours(startShift, endShift,d, (long) 1);

        return new ResponseEntity<>(wh, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/ride", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DriverWorkingHoursDTO> getRidesSorted(
            @PathVariable Integer id, @RequestParam int page, @RequestParam int size, @RequestParam String sort,
            @RequestParam String from, @RequestParam String to){

        return new ResponseEntity<>(new DriverWorkingHoursDTO(), HttpStatus.OK);
    }

    @GetMapping(value = "/working-hour/{id}")
    public ResponseEntity<WorkingHours> getOneWorkingHour(@PathVariable Integer id) {
        return new ResponseEntity<>(new WorkingHours(LocalDateTime.now(), LocalDateTime.now().plusHours(2),new Driver(), (long) id), HttpStatus.OK);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/working-hour/{id}")
    public ResponseEntity<WorkingHours> updateWorkingHours(@PathVariable Integer id) {
        WorkingHours wh = new WorkingHours(LocalDateTime.now().minusHours(4), LocalDateTime.now().minusHours(2),new Driver(), (long) id);
        wh.setEnd(LocalDateTime.now());
        return new ResponseEntity<>(wh, HttpStatus.OK);
    }
}
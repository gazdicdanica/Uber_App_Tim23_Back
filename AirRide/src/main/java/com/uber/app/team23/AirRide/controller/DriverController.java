package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.mapper.DriverDTOMapper;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Document;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.service.DriverService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "api/driver", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverController {

    @Autowired
    private DriverService driverService;


    @PostMapping
    public ResponseEntity<UserDTO> createDriver(@Valid @RequestBody Driver driver) throws ConstraintViolationException {
        Driver newDriver = driverService.save(driver);
        return new ResponseEntity<>(new UserDTO(newDriver), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserPaginatedDTO> getPaginatedDrivers(Pageable page) {
        Page<Driver> drivers = driverService.findAll(page);

        List<UserDTO> users = drivers.stream().map(DriverDTOMapper::fromDriverToDTO).collect(Collectors.toList());

        return new ResponseEntity<>(new UserPaginatedDTO(users), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getDriver(@PathVariable Long id) {
        Driver driver = driverService.findOne(id);
        return new ResponseEntity<>(new UserDTO(driver), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateDriver(@Valid @RequestBody Driver driverDTO, @PathVariable Integer id) {
        Driver driver = driverService.changeDriverData(driverService.findById((long) id), driverDTO);
        return new ResponseEntity<>(new UserDTO(driverService.update(driver)), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/documents")
    public ResponseEntity<DriverDocumentsDTO> getDriverDocuments(@PathVariable Integer id) {
        Driver driver = driverService.findById((long) id);
        DriverDocumentsDTO driverDocumentsDTO = driverService.getDocuments(driver);
        return new ResponseEntity<>(driverDocumentsDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/documents")
    public ResponseEntity<String> deleteDriverDocuments(@PathVariable Integer id) {
        Driver driver = driverService.findById((long) id);
        driverService.deleteDocsForDriver(driver);
        JSONObject json = new JSONObject();
        return new ResponseEntity<>(json.put("message", "Driver Deleted Successfully").toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/documents")
    public ResponseEntity<DriverDocumentsDTO> addDriverDocuments(@RequestBody DriverDocumentsDTO documentsDTO, @PathVariable Integer id) {
        Driver driver = driverService.findById((long) id);
        DriverDocumentsDTO document = driverService.saveDocsForDriver(driver, documentsDTO);
        // TODO validacija incoming dto objekata
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> getVehicleForDriver(@PathVariable Integer id) {
        Driver driver = driverService.findById((long) id);
        VehicleDTO vehicle = driverService.getVehicleForDriver(driver);

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> addVehicleToDriver(@PathVariable Integer id, @RequestBody VehicleDTO vehicleDTO) {
        driverService.findById((long) id);
        VehicleDTO vehicle = driverService.saveVehicleForDriver((long) id, vehicleDTO);
        // TODO Validacija Incoming DTO

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> updateDriverVehicle(@PathVariable Integer id, @RequestBody VehicleDTO vehicleDTO) {
        driverService.findById((long) id);
        VehicleDTO vehicle = driverService.updateVehicleForDriver((long) id, vehicleDTO);
        // TODO Razresiti lokaciju za vozilo
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }


    // TODO SVE OSTALO ZAVRSITI
    @GetMapping(value = "/{id}/working-hour")
    public ResponseEntity<DriverWorkingHoursDTO> getTotalWorkHours(
            @PathVariable Integer id, Pageable pageable) {

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
            @PathVariable Integer id, Pageable pageable){

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
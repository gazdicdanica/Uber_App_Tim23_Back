package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.mapper.*;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import com.uber.app.team23.AirRide.service.*;
import jakarta.validation.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "api/driver", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private WorkingHoursService workingHoursService;

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
    public ResponseEntity<Object> updateDriver(@Valid @RequestBody Driver driverDTO, @PathVariable Long id) {
        Driver driver = driverService.changeDriverData(driverService.findById(id), driverDTO, id);
        return new ResponseEntity<>(new UserDTO(driverService.update(driver)), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/documents")
    public ResponseEntity<DriverDocumentsDTO> getDriverDocuments(@PathVariable Long id) {
        Driver driver = driverService.findById(id);
        DriverDocumentsDTO driverDocumentsDTO = driverService.getDocuments(driver);
        return new ResponseEntity<>(driverDocumentsDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/documents")
    public ResponseEntity<String> deleteDriverDocuments(@PathVariable Long id) {
        Driver driver = driverService.findById(id);
        driverService.deleteDocsForDriver(driver);
        JSONObject json = new JSONObject();
        return new ResponseEntity<>(json.put("message", "Driver Deleted Successfully").toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/documents")
    public ResponseEntity<DriverDocumentsDTO> addDriverDocuments(@RequestBody DriverDocumentsDTO dto, @PathVariable Long id) {
        Driver driver = driverService.findById(id);
        DriverDocumentsDTO document = driverService.saveDocsForDriver(driver, dto);
        // TODO validation of incoming dto objects
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> getVehicleForDriver(@PathVariable Long id) {
        Driver driver = driverService.findById(id);
        VehicleDTO vehicle = driverService.getVehicleForDriver(driver);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> addVehicleToDriver(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        driverService.findById(id);
        VehicleDTO vehicle = driverService.saveVehicleForDriver(id, vehicleDTO);
        // TODO Validation of Incoming DTO

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> updateDriverVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        driverService.findById(id);
        VehicleDTO vehicle = driverService.updateVehicleForDriver(id, vehicleDTO);
        // TODO Resolve location for vehicle
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/working-hour")
    public ResponseEntity<DriverWorkingHoursDTO> getTotalWorkHours(@PathVariable Long id, Pageable pageable) {
        driverService.findById(id);
        Page<WorkingHours> workHours = workingHoursService.findAll(pageable);
        List<WorkHoursDTO> dto = workHours.stream().map(WorkHoursDTOMapper::fromWorkHoursToDTO).collect(Collectors.toList());

        return new ResponseEntity<>(new DriverWorkingHoursDTO(dto), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/working-hour")
    public ResponseEntity<WorkingHours> createDriverWorkingHours(@PathVariable Long id) {
        // Time generated when driver logged
        Driver d = driverService.findById(id);
        WorkingHours workingHours = workingHoursService.save(d);
        driverService.changeDriverStatus(true, id);

        return new ResponseEntity<>(workingHours, HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<RidePaginatedDTO> getRidesSorted(@PathVariable Long id, Pageable pageable){
        Page<Ride> rides = driverService.findAllRides(driverService.findById(id), pageable);
        List<RideResponseDTO> dto = rides.stream().map(RideDTOMapper::fromRideToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(new RidePaginatedDTO(dto), HttpStatus.OK);
    }

    @GetMapping(value = "/working-hour/{working-hour-id}")
    public ResponseEntity<WorkHoursDTO> getOneWorkingHour(@PathVariable("working-hour-id") Long id) {
        WorkingHours wh = workingHoursService.findOne(id);
        return new ResponseEntity<>(new WorkHoursDTO(wh.getStart(), wh.getEnd(), wh.getId()), HttpStatus.OK);
    }

    @PutMapping(value = "/working-hour/{working-hour-id}")
    public ResponseEntity<WorkHoursDTO> updateWorkingHours(@PathVariable("working-hour-id") Long id) {
        WorkingHours wh = workingHoursService.update(id);
        driverService.changeDriverStatus(false, wh.getDriver().getId());
        return new ResponseEntity<>(new WorkHoursDTO(wh.getStart(), wh.getEnd(), wh.getId()), HttpStatus.OK);
    }
}
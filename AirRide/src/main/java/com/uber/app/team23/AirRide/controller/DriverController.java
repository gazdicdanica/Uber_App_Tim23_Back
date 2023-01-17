package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.mapper.*;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.service.*;
import jakarta.validation.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    public ResponseEntity<WorkHoursDTO> createDriverWorkingHours(@PathVariable Long id, @RequestBody WorkHoursDTO workHoursDTO) {
        // Time generated when driver logged
        // TODO cannot start shift 400
        Driver d = driverService.findById(id);
        VehicleDTO vehicle = driverService.getVehicleForDriver(d);
        if(vehicle == null){
            throw new BadRequestException("Cannot start shift because the vehicle is not defined!");
        }
        WorkingHours workingHours = workingHoursService.save(d, workHoursDTO);
        driverService.changeDriverStatus(true, id);

        WorkHoursDTO dto = new WorkHoursDTO(workingHours.getStart(),null, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
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

    @PostMapping(value = "/{id}/working-hour/start")
    public ResponseEntity<WorkHoursDTO> startWorkingHours(@PathVariable Long id) {
        Driver d = driverService.findById(id);
        WorkingHours workingHours = workingHoursService.save(d, null);
        driverService.changeDriverStatus(true, id);

        WorkHoursDTO dto = new WorkHoursDTO(workingHours.getStart(),null, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/working-hour/end")
    public ResponseEntity<WorkHoursDTO> endWorkingHours(@PathVariable Long id){
        Driver d = driverService.findById(id);
        WorkingHours workingHours = workingHoursService.endWorkingHours(d);
        driverService.changeDriverStatus(false, id);

        return new ResponseEntity<>(new WorkHoursDTO(workingHours.getStart(), workingHours.getEnd(), id), HttpStatus.OK );
    }
}
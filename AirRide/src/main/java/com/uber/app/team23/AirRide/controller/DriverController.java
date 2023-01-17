package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.mapper.*;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Document;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/driver", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverController {
    int i = 0;
    @Autowired
    private DriverService driverService;

    @Autowired
    private WorkingHoursService workingHoursService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> createDriver(@Valid @RequestBody Driver driver) throws ConstraintViolationException {
        Driver newDriver = driverService.save(driver);
        return new ResponseEntity<>(new UserDTO(newDriver), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserPaginatedDTO> getPaginatedDrivers(Pageable page) {
        Page<Driver> drivers = driverService.findAll(page);
        List<UserDTO> users = drivers.stream().map(DriverDTOMapper::fromDriverToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(new UserPaginatedDTO(users), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getDriver(@PathVariable Long id) {
        Driver driver = driverService.findOne(id);
        return new ResponseEntity<>(new UserDTO(driver), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> updateDriver(@Valid @RequestBody UserDTO driverDTO, @PathVariable Long id) {
        Driver driver = driverService.changeDriverData(driverService.findById(id), driverDTO, id);
        return new ResponseEntity<>(new UserDTO(driverService.update(driver)), HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/{id}/documents")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DriverDocumentsDTO>> getDriverDocuments(@PathVariable Long id) {
        Driver driver = driverService.findById(id);
        List<DriverDocumentsDTO> respLi = driverService.getAllDocuments(driver);
        return new ResponseEntity<>(respLi, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/document/{document-id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteDriverDocuments(@PathVariable(name = "document-id") Long id) {
        Document document = driverService.findDocById(id);
        if (document == null) {
            throw new EntityNotFoundException("Document does not exist");
        }
        driverService.deleteDocsForDriver(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/{id}/documents")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<DriverDocumentsDTO> addDriverDocuments(@Valid @RequestBody DriverDocumentsDTO dto, @PathVariable Long id) {
        Driver driver = driverService.findById(id);
        DriverDocumentsDTO document = driverService.saveDocsForDriver(driver, dto);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/vehicle")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DRIVER')")
    public ResponseEntity<VehicleDTO> getVehicleForDriver(@PathVariable Long id) {
        Driver driver = driverService.findById(id);
        VehicleDTO vehicle = driverService.getVehicleForDriver(driver);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/vehicle")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<VehicleDTO> addVehicleToDriver(@PathVariable Long id, @Valid @RequestBody VehicleDTO vehicleDTO) {
        Driver driver = driverService.findById(id);
        VehicleDTO vehicle = driverService.saveVehicleForDriver(id, vehicleDTO);

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/vehicle")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<VehicleDTO> updateDriverVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDTO vehicleDTO) {
        driverService.findById(id);
        VehicleDTO vehicle = driverService.updateVehicleForDriver(id, vehicleDTO);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/working-hour")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DriverWorkingHoursDTO> getTotalWorkHours(@PathVariable Long id, Pageable pageable) {
        driverService.findById(id);
        Page<WorkingHours> workHours = workingHoursService.findAll(pageable);
        List<WorkHoursDTO> dto = workHours.stream().map(WorkHoursDTOMapper::fromWorkHoursToDTO).collect(Collectors.toList());

        return new ResponseEntity<>(new DriverWorkingHoursDTO(dto), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/working-hour")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WorkHoursDTO> createDriverWorkingHours(@PathVariable Long id, @Valid @RequestBody WorkHoursDTO workHoursDTO) {
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
    @PreAuthorize("hasAnyAuthority('ROLE_DRIVER', 'ROLE_ADMIN')")
    public ResponseEntity<RidePaginatedDTO> getRidesSorted(@PathVariable Long id, Pageable pageable){
        Page<Ride> rides = driverService.findAllRides(driverService.findById(id), pageable);
        List<RideResponseDTO> dto = rides.stream().map(RideDTOMapper::fromRideToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(new RidePaginatedDTO(dto), HttpStatus.OK);
    }

    @GetMapping(value = "/working-hour/{working-hour-id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WorkHoursDTO> getOneWorkingHour(@PathVariable("working-hour-id") Long id) {
        WorkingHours wh = workingHoursService.findOne(id);
        return new ResponseEntity<>(new WorkHoursDTO(wh.getStart(), wh.getEnd(), wh.getId()), HttpStatus.OK);
    }

    @PutMapping(value = "/working-hour/{working-hour-id}")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<WorkHoursDTO> updateWorkingHours(@PathVariable("working-hour-id") Long id) {
        WorkingHours wh = workingHoursService.update(id);
        driverService.changeDriverStatus(false, wh.getDriver().getId());
        return new ResponseEntity<>(new WorkHoursDTO(wh.getStart(), wh.getEnd(), wh.getId()), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/working-hour/start")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<WorkHoursDTO> startWorkingHours(@PathVariable Long id) {
        Driver d = driverService.findById(id);
        WorkingHours workingHours = workingHoursService.save(d, null);
        driverService.changeDriverStatus(true, id);

        WorkHoursDTO dto = new WorkHoursDTO(workingHours.getStart(),null, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/working-hour/end")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<WorkHoursDTO> endWorkingHours(@PathVariable Long id){
        Driver d = driverService.findById(id);
        WorkingHours workingHours = workingHoursService.endWorkingHours(d);
        driverService.changeDriverStatus(false, id);

        return new ResponseEntity<>(new WorkHoursDTO(workingHours.getStart(), workingHours.getEnd(), id), HttpStatus.OK );
    }

}
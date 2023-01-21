package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.mapper.*;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.User;
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

import javax.print.Doc;
import java.util.ArrayList;
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

    @GetMapping(value = "/{id}/location")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<Location> getDriverLocation(@PathVariable Long id) {
        Driver driver = driverService.findById(id);
        VehicleDTO vehicle = driverService.getVehicleForDriver(driver);
        return new ResponseEntity<>(vehicle.getCurrentLocation(), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserPaginatedDTO> getPaginatedDrivers(Pageable page) {
        Page<Driver> drivers = driverService.findAll(page);

        List<UserDTO> users = drivers.stream().map(DriverDTOMapper::fromDriverToDTO).collect(Collectors.toList());

        return new ResponseEntity<>(new UserPaginatedDTO(users), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DRIVER')")
    public ResponseEntity<UserDTO> getDriver(@PathVariable Long id) {
        Driver driver = driverService.findOne(id);
        return new ResponseEntity<>(new UserDTO(driver), HttpStatus.OK);
    }

    @Transactional
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DRIVER')")
    public ResponseEntity<Object> updateDriver(@Valid @RequestBody UserDTO driverDTO, @PathVariable Long id) {
        Driver driver = driverService.changeDriverData(driverService.findById(id), driverDTO);
        System.err.println("DTO");
        System.err.println(driverDTO.getProfilePicture());
        UserDTO ret = new UserDTO(driverService.update(driver));
        System.err.println("RET");
        System.err.println(ret.getProfilePicture());
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/documents")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DRIVER')")
    public ResponseEntity<List<DriverDocumentsDTO>> getDriverDocuments(@PathVariable Long id) {
        Driver driver = driverService.findById(id);
        List<Document> documentList = driverService.getDocuments(driver);
        List<DriverDocumentsDTO> resp = new ArrayList<>(); //= documentList.stream().map(DocumentDTOMapper::fromDocToDTO).collect(Collectors.toList());
        for(Document doc : documentList){
            DriverDocumentsDTO dto = new DriverDocumentsDTO(doc);
            resp.add(dto);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @DeleteMapping(value = "/document/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteDriverDocuments(@PathVariable Long id) {
        driverService.deleteDocsById(id);
        return new ResponseEntity<>("Document Deleted Successfully", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/document")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<String> deleteDocument(@RequestParam(value = "name") String value){
        driverService.deleteDocumentByName(value);
        System.err.println("DELETED");
        JSONObject obj = new JSONObject();
        obj.put("message", "Document deleted successfully");
        return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/documents")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DRIVER')")
    public ResponseEntity<DriverDocumentsDTO> addDriverDocuments(@Valid @RequestBody DriverDocumentsDTO dto, @PathVariable Long id) {
        Driver driver = driverService.findById(id);
        System.err.println("ADD DOCUMENT");
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
    public ResponseEntity<VehicleDTO> addVehicleToDriver(@PathVariable Long id, @Valid @RequestBody VehicleDTO vehicleDTO) {
        driverService.findById(id);
        VehicleDTO vehicle = driverService.saveVehicleForDriver(id, vehicleDTO);

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @Transactional
    @PutMapping(value = "/{id}/vehicle")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DRIVER')")
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
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RidePaginatedDTO> getRidesSorted(@PathVariable Long id, Pageable pageable){
        Page<Ride> rides = driverService.findAllRides(driverService.findById(id), pageable);
        List<RideResponseDTO> dto = rides.stream().map(RideDTOMapper::fromRideToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(new RidePaginatedDTO(dto), HttpStatus.OK);
    }

    @GetMapping(value = "/working-hour/{working-hour-id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DRIVER')")
    public ResponseEntity<WorkHoursDTO> getOneWorkingHour(@PathVariable("working-hour-id") Long id) {
        WorkingHours wh = workingHoursService.findOne(id);
        return new ResponseEntity<>(new WorkHoursDTO(wh.getStart(), wh.getEnd(), wh.getId()), HttpStatus.OK);
    }

    @PutMapping(value = "/working-hour/{working-hour-id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DRIVER')")
    public ResponseEntity<WorkHoursDTO> updateWorkingHours(@PathVariable("working-hour-id") Long id) {
        WorkingHours wh = workingHoursService.update(id);
        driverService.changeDriverStatus(false, wh.getDriver().getId());
        return new ResponseEntity<>(new WorkHoursDTO(wh.getStart(), wh.getEnd(), wh.getId()), HttpStatus.OK);
    }


    @PutMapping(value = "/{id}/working-hour/start")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DRIVER')")
    @Transactional
    public ResponseEntity<WorkHoursDTO> startWorkingHours(@PathVariable Long id) {
        Driver d = driverService.findById(id);
        WorkingHours workingHours = workingHoursService.save(d, null);
        System.err.println(workingHours.toString());
        driverService.changeDriverStatus(true, id);

        WorkHoursDTO dto = new WorkHoursDTO(workingHours.getStart(),null, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/working-hour/end")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DRIVER')")
    @Transactional
    public ResponseEntity<WorkHoursDTO> endWorkingHours(@PathVariable Long id){
        Driver d = driverService.findById(id);
        WorkingHours workingHours = workingHoursService.endWorkingHours(d);
        driverService.changeDriverStatus(false, id);
        return new ResponseEntity<>(new WorkHoursDTO(workingHours.getStart(), workingHours.getEnd(), id), HttpStatus.OK );
    }

}
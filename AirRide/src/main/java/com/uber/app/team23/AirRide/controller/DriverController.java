package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.DriverDTO;
import com.uber.app.team23.AirRide.dto.DriverDocumentsDTO;
import com.uber.app.team23.AirRide.dto.DriverWorkingHoursDTO;
import com.uber.app.team23.AirRide.dto.VehicleDTO;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController @RequestMapping("api/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DriverDTO> createDriver(@RequestBody DriverDTO driverDTO) {
        Driver driver = new Driver();
        driver.setId((long)123);
        driver.setName(driverDTO.getName());
        driver.setLastName(driverDTO.getSurname());
        driver.setProfilePhoto(driverDTO.getProfilePicture());
        driver.setPhoneNumber(driverDTO.getTelephoneNumber());
        driver.setEmail(driverDTO.getEmail());
        driver.setAddress(driverDTO.getAddress());

//        driver = driverService.save(driver);
        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DriverDTO> getDriver(@PathVariable Integer id) {
//        Driver driver = driverService.findOne(id);
//        if (driver == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }

        Driver driver = new Driver();
        driver.setId((long)id);
        driver.setName("Pera");
        driver.setLastName("Perić");
        driver.setProfilePhoto("U3dhZ2dlciByb2Nrcw==");
        driver.setPhoneNumber("+381123123");
        driver.setEmail("pera.peric@email.com");
        driver.setAddress("Bulevar Oslobodjenja 74");
        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.OK);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@RequestBody DriverDTO driverDTO, @PathVariable Integer id) {
        Driver driver = new Driver();
        driver.setId((long) id);
        driver.setName(driverDTO.getName());
        driver.setLastName(driverDTO.getSurname());
        driver.setProfilePhoto(driverDTO.getProfilePicture());
        driver.setPhoneNumber(driverDTO.getTelephoneNumber());
        driver.setEmail(driverDTO.getEmail());
        driver.setAddress(driverDTO.getAddress());

        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/documents")
    public ResponseEntity<DriverDocumentsDTO> getDriverDocuments(@PathVariable Integer id) {
        Driver driver = new Driver();
        driver.setId((long)10);

        DriverDocumentsDTO documentsDTO = new DriverDocumentsDTO((long)123, "Vozačka dozvola",
                "U3dhZ2dlciByb2Nrcw=", driver.getId());

        return new ResponseEntity<>(documentsDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/documents")
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
        vehicle.setVehicleType(VehicleEnum.STANDARD);
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

    @GetMapping(value = "/{id}/working-hours")
    public ResponseEntity<DriverWorkingHoursDTO> getTotalWorkHours(@PathVariable Integer id) {
        WorkingHours wh1 = new WorkingHours(LocalDateTime.now(), LocalDateTime.now().plusHours(2), (long) 1);
        WorkingHours wh2 = new WorkingHours(LocalDateTime.now(), LocalDateTime.now().plusHours(2), (long) 1);
        DriverWorkingHoursDTO workingHoursDTO = new DriverWorkingHoursDTO();
        workingHoursDTO.updateWorkingHours(wh1);
        workingHoursDTO.updateWorkingHours(wh2);

        return new ResponseEntity<>(workingHoursDTO, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/working-hours")
    public ResponseEntity<DriverWorkingHoursDTO> updateDriverWorkingHours(@PathVariable Integer id) {
        // Time generated when driver logged
        LocalDateTime startShift = LocalDateTime.now().minusHours(3);
        // Time generated when driver finished shift
        LocalDateTime endShift = LocalDateTime.now();
        WorkingHours wh = new WorkingHours(startShift, endShift, (long) 1);
        DriverWorkingHoursDTO whDTO = new DriverWorkingHoursDTO();
        whDTO.updateWorkingHours(wh);

        return new ResponseEntity<>(whDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/working-hour/{id}")
    public ResponseEntity<WorkingHours> getOneWorkingHour(@PathVariable Integer id) {
        return new ResponseEntity<>(new WorkingHours(LocalDateTime.now(), LocalDateTime.now().plusHours(2), (long) id), HttpStatus.OK);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/working-hour/{id}")
    public ResponseEntity<WorkingHours> updateWorkingHours(@PathVariable Integer id) {
        WorkingHours wh = new WorkingHours(LocalDateTime.now().minusHours(4), LocalDateTime.now().minusHours(2), (long) id);
        wh.setEnd(LocalDateTime.now());
        return new ResponseEntity<>(wh, HttpStatus.OK);
    }
}

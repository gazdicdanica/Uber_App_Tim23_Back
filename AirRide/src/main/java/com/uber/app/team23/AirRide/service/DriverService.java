package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.DriverDocumentsDTO;
import com.uber.app.team23.AirRide.dto.VehicleDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.mapper.VehicleDTOMapper;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Document;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import com.uber.app.team23.AirRide.repository.DocumentRepository;
import com.uber.app.team23.AirRide.repository.DriverRepository;
import com.uber.app.team23.AirRide.repository.VehicleRepository;
import com.uber.app.team23.AirRide.repository.VehicleTypeRepository;
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
    private VehicleRepository vehicleRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

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
            throw new BadRequestException(USER_ALREADY_EXISTS);
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
            throw new EntityNotFoundException(USER_DOES_NOT_EXIST);
        }
        return driver;
    }
    public Driver findOne(Long id) throws NullPointerException {
        Driver driver = driverRepository.findById(id).orElse(null);
        if (driver == null) {
            throw new EntityNotFoundException(USER_DOES_NOT_EXIST);
        }
        return driver;
    }

    public Driver findById(Long id) {
        Driver driver = driverRepository.findById(id).orElse(null);
        if (driver == null) {
            throw new EntityNotFoundException(USER_DOES_NOT_EXIST);
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

        DriverDocumentsDTO dto = new DriverDocumentsDTO(document.getId(), document.getName(), document.getDocumentImage(), document.getDriver().getId());
        return dto;
    }

    public void deleteDocsForDriver(Driver driver) {
        Document document = documentRepository.findAllByDriverId(driver.getId());
        if (document == null) {
            throw new EntityNotFoundException("Documents for this driver do not exist");
        }
        documentRepository.deleteById(document.getId());
    }

    public DriverDocumentsDTO saveDocsForDriver(Driver driver, DriverDocumentsDTO documentsDTO) {
        Document document = new Document();
        document.setDriver(driver);
        document.setName(documentsDTO.getName());
        document.setDocumentImage(documentsDTO.getDocumentImage());

        document = documentRepository.save(document);

        documentsDTO.setDriverId(driver.getId());
        documentsDTO.setId(document.getId());
        return documentsDTO;
    }

    public VehicleDTO getVehicleForDriver(Driver driver) {
        Vehicle vehicle = vehicleRepository.findAllByDriverId(driver.getId());
        if (vehicle == null) {
            throw new EntityNotFoundException("Vehicle For This Driver Does Not Exist");
        }
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setDriverId(driver.getId());
        vehicleDTO.setVehicleType(vehicle.getVehicleType().getType());
        vehicleDTO.setModel(vehicle.getVehicleModel());
        vehicleDTO.setLicenseNumber(vehicle.getLicenseNumber());
        vehicleDTO.setPassengerSeats(vehicleDTO.getPassengerSeats());
        vehicleDTO.setBabyTransport(vehicle.isBabyTransport());
        vehicleDTO.setPetTransport(vehicle.isPetTransport());

        // TODO Add table VehicleLocation
        vehicleDTO.setCurrentLocation(new Location());


        return vehicleDTO;
    }

    public VehicleDTO saveVehicleForDriver(Long driverId, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findAllByDriverId(driverId);
        if (vehicle != null) {
            throw new BadRequestException("Driver Already Has Vehicle");
        }
        Driver driver = driverRepository.findById(driverId).orElse(null);
        Vehicle toSave = new Vehicle();
        toSave.setDriver(driver);
        toSave.setVehicleModel(vehicleDTO.getModel());
        // TODO Razresiti Cenu za Vozilo
        VehicleType vt = new VehicleType(null, vehicleDTO.getVehicleType(), 300);
        vehicleTypeRepository.save(vt);
        toSave.setVehicleType(vt);
        toSave.setLicenseNumber(vehicleDTO.getLicenseNumber());
        toSave.setCapacity(vehicleDTO.getPassengerSeats());
        toSave.setBabyTransport(vehicleDTO.isBabyTransport());
        toSave.setPetTransport(vehicleDTO.isPetTransport());
        vehicle = vehicleRepository.save(toSave);

        vehicleDTO.setDriverId(vehicle.getDriver().getId());
        vehicleDTO.setId(vehicle.getId());
        return vehicleDTO;
    }

    public VehicleDTO updateVehicleForDriver(long driverId, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findAllByDriverId(driverId);
        if (vehicle == null) {
            return saveVehicleForDriver(driverId, vehicleDTO);
        } else {
            vehicle.setVehicleModel(vehicleDTO.getModel());
            vehicle.setLicenseNumber(vehicleDTO.getLicenseNumber());
            vehicle.setCapacity(vehicleDTO.getPassengerSeats());
            vehicle.setPetTransport(vehicleDTO.isPetTransport());
            vehicle.setBabyTransport(vehicleDTO.isBabyTransport());
            if (vehicle.getVehicleType().getType() != vehicleDTO.getVehicleType()){
                VehicleType vt = new VehicleType();
                vt.setType(vehicleDTO.getVehicleType());
                vt.setPrice(400);
                vehicleTypeRepository.save(vt);
                // TODO price
            }
            vehicleRepository.save(vehicle);

            return VehicleDTOMapper.fromVehicleToDTO(vehicle);
        }
    }
}

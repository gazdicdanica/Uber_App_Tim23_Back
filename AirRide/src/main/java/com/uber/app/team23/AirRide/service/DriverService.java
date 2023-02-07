package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.DriverDocumentsDTO;
import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.dto.UserDTO;
import com.uber.app.team23.AirRide.dto.VehicleDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.mapper.VehicleDTOMapper;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Document;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import com.uber.app.team23.AirRide.repository.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
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
    private RoleService roleService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private RideRepository rideRepository;

    public Page<Driver> findAll(Pageable page) {
        return driverRepository.findAll(page);
    }

    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    public List<Driver> findOnlineDrivers() {return this.driverRepository.findAllByOnline(true);}

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
        newDriver.setActive(true);

        List<Role> li = new ArrayList<>();
        li.add(roleService.findByName("ROLE_DRIVER"));
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
            throw new EntityNotFoundException("Driver does not exist");
        }
        return driver;
    }

    public Driver findById(Long id){
        System.err.println("err");
        System.err.println(driverRepository.findById(id));
        return driverRepository.findById(id).orElseThrow(()->new EntityNotFoundException(USER_DOES_NOT_EXIST));
    }

    public Driver changeDriverData(Driver driver, UserDTO driverDTO) {
        driver.setName(driverDTO.getName());
        driver.setSurname(driverDTO.getSurname());
        driver.setProfilePicture(Base64.getDecoder().decode(driverDTO.getProfilePicture()));
        driver.setTelephoneNumber(driverDTO.getTelephoneNumber());
        driver.setEmail(driverDTO.getEmail());
        driver.setAddress(driverDTO.getAddress());
//        driver.setPassword(driverRepository.findPasswordById(id));

        return driver;
    }

    public List<Document> getDocuments(Driver driver) {
        return documentRepository.findAllByDriver(driver);
    }

    public void deleteDocsForDriver(Driver driver) {
        List<Document> documents = documentRepository.findAllByDriver(driver);
        if (documents == null) {
            throw new EntityNotFoundException("Documents for this driver do not exist");
        }
        for (Document doc : documents
             ) {
            if(doc.getDriver() == driver) {
                documentRepository.deleteById(doc.getId());
            }
        }
    }

    public DriverDocumentsDTO saveDocsForDriver(Driver driver, DriverDocumentsDTO documentsDTO) {
        Document document = new Document();
        document.setDriver(driver);
        document.setName(documentsDTO.getName());
        document.setDocumentImage(Base64.getDecoder().decode(documentsDTO.getDocumentImage()));

        document = documentRepository.save(document);

        documentsDTO.setDriverId(driver.getId());
        documentsDTO.setId(document.getId());
        return documentsDTO;
    }

    public VehicleDTO getVehicleForDriver(Driver driver) {
        Vehicle vehicle = vehicleRepository.findByDriver(driver.getId());
        if (vehicle == null) {
            throw new EntityNotFoundException("Vehicle For This Driver Does Not Exist");
        }
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setDriverId(driver.getId());
        vehicleDTO.setVehicleType(vehicle.getVehicleType().getType());
        vehicleDTO.setModel(vehicle.getVehicleModel());
        vehicleDTO.setLicenseNumber(vehicle.getLicenseNumber());
        vehicleDTO.setPassengerSeats(vehicle.getPassengerSeats());
        vehicleDTO.setBabyTransport(vehicle.isBabyTransport());
        vehicleDTO.setPetTransport(vehicle.isPetTransport());
        vehicleDTO.setCurrentLocation(vehicle.getCurrentLocation());


        return vehicleDTO;
    }

    public VehicleDTO saveVehicleForDriver(Long driverId, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findByDriver(driverId);
        if (vehicle != null) {
            throw new BadRequestException("Driver Already Has Vehicle");
        }
        Driver driver = driverRepository.findById(driverId).orElse(null);
        Vehicle toSave = new Vehicle();
        toSave.setDriver(driver);
        toSave.setVehicleModel(vehicleDTO.getModel());

        VehicleType vt = vehicleTypeRepository.findByType(vehicleDTO.getVehicleType()).orElse(null);
        toSave.setVehicleType(vt);
        toSave.setLicenseNumber(vehicleDTO.getLicenseNumber());
        toSave.setPassengerSeats(vehicleDTO.getPassengerSeats());
        toSave.setBabyTransport(vehicleDTO.isBabyTransport());
        toSave.setPetTransport(vehicleDTO.isPetTransport());
        vehicle = vehicleRepository.save(toSave);

        vehicleDTO.setDriverId(vehicle.getDriver().getId());
        vehicleDTO.setId(vehicle.getId());
        return vehicleDTO;
    }

    public VehicleDTO updateVehicleForDriver(long driverId, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findByDriver(driverId);
        if (vehicle == null) {
            throw new EntityNotFoundException("Vehicle not found");
        } else {
            vehicle.setVehicleModel(vehicleDTO.getModel());
            vehicle.setLicenseNumber(vehicleDTO.getLicenseNumber());
            vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());
            vehicle.setPetTransport(vehicleDTO.isPetTransport());
            vehicle.setBabyTransport(vehicleDTO.isBabyTransport());
            if (vehicle.getVehicleType().getType() != vehicleDTO.getVehicleType()){
                VehicleType vt = vehicleTypeRepository.findByType(vehicleDTO.getVehicleType()).orElse(null);
                vehicle.setVehicleType(vt);
            }
            vehicleRepository.save(vehicle);

            return VehicleDTOMapper.fromVehicleToDTO(vehicle);
        }
    }

    public void changeDriverStatus(boolean online, Long id){
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Driver does not exist"));
        driver.setOnline(online);
        driverRepository.save(driver);
    }
    
    public Page<Ride> findAllRides(User byId, Pageable pageable) {
        return rideRepository.findAllByDriver(byId, pageable);
    }

    public Ride findRideById(Long rideId) {
        return rideRepository.findById(rideId).orElse(null);
    }

    public void deleteDocsById(Long id) {
        documentRepository.deleteById(id);
    }

    public void deleteDocumentByName(String name){
        documentRepository.deleteByName(name);
    }

    public RideStatus findDriverStatus(Driver driver) {
        Driver d = this.findById(driver.getId());
        Ride esp = rideRepository.findByDriverAndStatus(d, RideStatus.ACTIVE).orElse(null);
        Ride isPanic = rideRepository.findByDriverAndStatus(d, RideStatus.PANIC).orElse(null);
        if (isPanic == null) {
            if (esp == null) {
                return RideStatus.FINISHED;
            } else {
                return RideStatus.ACTIVE;
            }
        } else {
            return RideStatus.PANIC;
        }
    }
}

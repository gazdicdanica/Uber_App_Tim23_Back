package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.controller.WebSocketController;
import com.uber.app.team23.AirRide.dto.VehicleDTO;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.mapper.VehicleDTOMapper;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import com.uber.app.team23.AirRide.repository.VehicleRepository;
import com.uber.app.team23.AirRide.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    LocationService locationService;
    @Autowired
    VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    WebSocketController webSocketController;

    @Scheduled(fixedRate = 1000 * 10)
    public void updateVehiclesLocation() {
        List<Vehicle> vehicles = this.vehicleRepository.findAll();
        List<VehicleDTO> dto = vehicles.stream().map(VehicleDTOMapper::fromVehicleToDTO).collect(Collectors.toList());
            webSocketController.simpMessagingTemplate.convertAndSend("/update-vehicle-location/", dto);
    }

    public Vehicle findOne(Long id){
        return this.vehicleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Vehicle does not exist"));
    }

    public void changeLocation(Long id, Location location){
        Vehicle vehicle = findOneByDriverId(id);
        if (vehicle == null) {
            throw new EntityNotFoundException("Vehicle does not exist");
        }
        Location l = locationService.findByAddress(location.getAddress());
        if(l == null){
            l = locationService.save(location);
        }
        vehicle.setCurrentLocation(l);
        vehicleRepository.save(vehicle);
    }

    private Vehicle findOneByDriverId(Long id) {
        return vehicleRepository.findByDriver(id);
    }
    
    public List<VehicleType> findAllVehicleTypes(){
        return this.vehicleTypeRepository.findAll();
    }

}

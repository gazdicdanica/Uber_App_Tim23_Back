package com.uber.app.team23.AirRide.mapper;

import com.uber.app.team23.AirRide.dto.VehicleDTO;import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VehicleDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public VehicleDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static VehicleDTO fromVehicleToDTO(Vehicle vehicle) {
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

}

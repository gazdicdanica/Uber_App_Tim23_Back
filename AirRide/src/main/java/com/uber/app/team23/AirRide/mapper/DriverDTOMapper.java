package com.uber.app.team23.AirRide.mapper;

import com.uber.app.team23.AirRide.dto.UserDTO;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public DriverDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static UserDTO fromDriverToDTO(Driver driver) {
        return modelMapper.map(driver, UserDTO.class);
    }
}

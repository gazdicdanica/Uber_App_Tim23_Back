package com.uber.app.team23.AirRide.mapper;

import com.uber.app.team23.AirRide.dto.UserDTO;
import com.uber.app.team23.AirRide.model.users.Passenger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PassengerDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public PassengerDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static UserDTO fromPassengerToDTO(Passenger passenger){
        return modelMapper.map(passenger, UserDTO.class);
    }
}

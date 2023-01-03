package com.uber.app.team23.AirRide.mapper;

import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RideDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public RideDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public static RideResponseDTO fromRideToDTO(Ride ride) { return modelMapper.map(ride, RideResponseDTO.class); }

}

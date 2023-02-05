package com.uber.app.team23.AirRide.mapper;

import com.uber.app.team23.AirRide.dto.PanicDTO;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PanicDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public PanicDTOMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public static PanicDTO fromPanicToDTO(Panic panic) { return modelMapper.map(panic, PanicDTO.class);}
}

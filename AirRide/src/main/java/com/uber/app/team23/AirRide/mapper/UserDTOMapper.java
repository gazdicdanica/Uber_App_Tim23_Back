package com.uber.app.team23.AirRide.mapper;

import com.uber.app.team23.AirRide.dto.UserDTO;
import com.uber.app.team23.AirRide.model.users.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public UserDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static UserDTO fromUserToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}

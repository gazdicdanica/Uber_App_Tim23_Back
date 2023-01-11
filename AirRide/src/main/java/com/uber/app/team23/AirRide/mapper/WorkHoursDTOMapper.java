package com.uber.app.team23.AirRide.mapper;

import com.uber.app.team23.AirRide.dto.WorkHoursDTO;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkHoursDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public WorkHoursDTOMapper(ModelMapper modelMapper) { this.modelMapper = modelMapper;}

    public static WorkHoursDTO fromWorkHoursToDTO(WorkingHours workingHours) { return modelMapper.map(workingHours, WorkHoursDTO.class);}
}

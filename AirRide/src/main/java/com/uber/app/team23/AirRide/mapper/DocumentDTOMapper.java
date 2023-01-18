package com.uber.app.team23.AirRide.mapper;

import com.uber.app.team23.AirRide.dto.DriverDocumentsDTO;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Document;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public DocumentDTOMapper(ModelMapper modelMapper) { this.modelMapper = modelMapper; }

    public static DriverDocumentsDTO fromDocToDTO(Document document) {
        return modelMapper.map(document, DriverDocumentsDTO.class);
    }
}

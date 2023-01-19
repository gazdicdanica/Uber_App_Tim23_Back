package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.mapper.DocumentDTOMapper;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Document;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Base64;

@Data @AllArgsConstructor @NoArgsConstructor
public class DriverDocumentsDTO {
    private long id;

    @NotNull
    @Size(min = 5, max = 30)
    private String name;

    @NotNull
    private String documentImage;

    private Long driverId;


    public DriverDocumentsDTO(Document document){
        this.name = document.getName();
        this.documentImage = Base64.getEncoder().encodeToString(document.getDocumentImage());
        this.driverId = document.getDriver().getId();
    }
}

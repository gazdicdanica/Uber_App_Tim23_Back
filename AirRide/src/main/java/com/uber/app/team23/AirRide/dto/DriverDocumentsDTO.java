package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class DriverDocumentsDTO {
    private long id;
    private String name;
    private String documentImage;
    private Long driverId;


    public DriverDocumentsDTO(long id, String name, String documentImage, Long driverId) {
        this.id = id;
        this.name = name;
        this.documentImage = documentImage;
        this.driverId = driverId;
    }
}

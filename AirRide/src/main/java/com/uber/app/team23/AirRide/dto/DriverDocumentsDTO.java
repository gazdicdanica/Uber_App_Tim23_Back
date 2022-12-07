package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DriverDocumentsDTO {
    private long id;
    private String name;
    private String documentImage;
    private Long driverId;

}

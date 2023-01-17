package com.uber.app.team23.AirRide.dto;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class DriverDocumentsDTO {
    private long id;
    private String name;
    private String documentImage;
    private Long driverId;

}

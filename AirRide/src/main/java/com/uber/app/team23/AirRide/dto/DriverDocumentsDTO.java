package com.uber.app.team23.AirRide.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class DriverDocumentsDTO {
    private long id;
    @NotNull
    @Size(min = 5, max = 20)
    private String name;

    @NotNull
    private String documentImage;

    private Long driverId;

}

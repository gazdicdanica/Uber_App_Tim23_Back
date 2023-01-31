package com.uber.app.team23.AirRide.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class DateRangeDTO {
    private LocalDateTime start;
    private LocalDateTime end;
}

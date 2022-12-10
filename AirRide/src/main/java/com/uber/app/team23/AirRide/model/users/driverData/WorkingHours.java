package com.uber.app.team23.AirRide.model.users.driverData;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @NoArgsConstructor
public class WorkingHours {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long id;

    public WorkingHours(LocalDateTime start, LocalDateTime end, Long id) {
        this.start = start;
        this.end = end;
        this.id = id;
    }
}

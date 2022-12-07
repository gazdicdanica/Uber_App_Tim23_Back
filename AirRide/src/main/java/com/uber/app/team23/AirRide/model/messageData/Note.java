package com.uber.app.team23.AirRide.model.messageData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Note {
    private Long id;
    private LocalDateTime date;
    private String message;
}

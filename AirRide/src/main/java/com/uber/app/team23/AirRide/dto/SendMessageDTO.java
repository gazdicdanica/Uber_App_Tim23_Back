package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.messageData.MessageType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class SendMessageDTO {
    @NotNull @NotEmpty @Size(min = 1 , max = 50)
    private String message;
    @NotNull
    private MessageType type;
    @NotNull
    private Long rideId;
}

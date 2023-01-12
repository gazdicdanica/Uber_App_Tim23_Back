package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.messageData.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class SendMessageDTO {
    private String message;
    private MessageType type;
    private Long rideId;
}

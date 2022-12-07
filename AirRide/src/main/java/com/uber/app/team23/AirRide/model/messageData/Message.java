package com.uber.app.team23.AirRide.model.messageData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Message {
    public Long senderId;
    public Long receiverId;
    public String message;
    public LocalDateTime sendTime;
    public MessageType type;
    public Long id;
    public Long rideId;

}

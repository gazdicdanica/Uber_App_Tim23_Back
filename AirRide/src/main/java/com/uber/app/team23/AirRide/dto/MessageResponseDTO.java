package com.uber.app.team23.AirRide.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uber.app.team23.AirRide.model.messageData.Message;
import com.uber.app.team23.AirRide.model.messageData.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class MessageResponseDTO {
    private Long id;
    private Long sender;
    private Long receiver;
    private String message;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeOfSending;
    private MessageType type;
    private Long ride;

    public MessageResponseDTO(Message message) {
        this.id = message.getId();
        this.sender = message.getSender().getId();
        this.receiver = message.getReceiver().getId();
        this.message = message.getMessage();
        this.timeOfSending = message.getTimeOfSending();
        this.type = message.getType();
        this.ride = message.getRide().getId();
    }
}

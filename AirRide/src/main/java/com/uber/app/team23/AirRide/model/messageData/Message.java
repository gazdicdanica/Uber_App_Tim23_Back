package com.uber.app.team23.AirRide.model.messageData;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uber.app.team23.AirRide.dto.SendMessageDTO;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "messages")
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    public User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    public User receiver;
    @Column(name = "message", nullable = false)
    public String message;
    @Column(name = "time_of_sending")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public LocalDateTime timeOfSending;
    @Column(name = "message_type")
    public MessageType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id")
    public Ride ride;

    public Message(SendMessageDTO dto, Ride ride, User receiver, User sender) {
        this.sender = sender;
        this.message = dto.getMessage();
        this.type = dto.getType();
        this.ride = ride;
        this.timeOfSending = LocalDateTime.now();
        this.receiver = receiver;
    }

}

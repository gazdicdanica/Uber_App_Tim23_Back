package com.uber.app.team23.AirRide.model.messageData;

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
    public LocalDateTime timeOfSending;
    @Column(name = "message_type")
    public MessageType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id")
    public Ride ride;

}

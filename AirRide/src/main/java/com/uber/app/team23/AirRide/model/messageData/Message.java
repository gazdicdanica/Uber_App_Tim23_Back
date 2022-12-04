package com.uber.app.team23.AirRide.model.messageData;

import com.uber.app.team23.AirRide.model.users.User;

import java.time.LocalDateTime;

public class Message {
    public User sender;
    public User receiver;
    public String msgContent;
    public LocalDateTime sendTime;
    public MessageType msgType;
    public Long id;

}

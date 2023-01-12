package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.RideMessages;
import com.uber.app.team23.AirRide.model.messageData.Message;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    public List<Message> findAllForUser(User u) {
        return messageRepository.findAllForUser(u);
    }

    public Message save(Message msg) {
        return messageRepository.save(msg);
    }
}

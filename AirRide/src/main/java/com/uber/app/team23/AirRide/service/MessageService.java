package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.MessageResponseDTO;
import com.uber.app.team23.AirRide.dto.RideMessages;
import com.uber.app.team23.AirRide.model.messageData.Message;
import com.uber.app.team23.AirRide.model.rideData.Ride;
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

    public List<MessageResponseDTO> findAllByUsersForRide(User u1, User u2, Ride r) {return messageRepository.findAllByUsersForRide(u1, u2, r);}
}

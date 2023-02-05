package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.dto.MessageResponseDTO;
import com.uber.app.team23.AirRide.model.messageData.Message;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.sender=?1 or m.receiver=?1")
    List<Message> findAllForUser(User user);

    @Query("select distinct new com.uber.app.team23.AirRide.dto.MessageResponseDTO(m) from Message m left join Ride r where ((m.sender=?1 and m.receiver=?2) or (m.sender=?2 and m.receiver=?1)) and m.ride=?3")
    List<MessageResponseDTO> findAllByUsersForRide(User u1, User u2, Ride r);
}

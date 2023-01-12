package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.messageData.Message;
import com.uber.app.team23.AirRide.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.sender=?1 or m.receiver=?1")
    List<Message> findAllForUser(User user);
}

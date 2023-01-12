package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.messageData.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

    public Page<Note> findAllByUserId(Long user, Pageable page);
}

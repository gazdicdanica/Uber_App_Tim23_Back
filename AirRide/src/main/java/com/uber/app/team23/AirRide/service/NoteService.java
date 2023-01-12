package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.model.messageData.Note;
import com.uber.app.team23.AirRide.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public Page<Note> findAll(Long user, Pageable page) {
        return noteRepository.findAllByUserId(user, page);
    }

    public Note save(Note note) {
        return noteRepository.save(note);
    }
}

package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.messageData.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class NoteDTO {
    private int totalCount;
    private List<Note> noteLi = new ArrayList<>();

    public void addNote(Note note) {
        this.noteLi.add(note);
    }
}

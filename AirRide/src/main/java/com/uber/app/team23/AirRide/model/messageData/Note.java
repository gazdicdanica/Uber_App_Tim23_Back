package com.uber.app.team23.AirRide.model.messageData;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "notes")
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_Id")
    private Long userId;

    @Column(name = "creation_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime date;

    @Column(name = "message")
    private String message;

    public Note(AdminNoteDTO dto, Long userId){
        this.message = dto.getMessage();
        this.date = LocalDateTime.now();
        this.userId = userId;
    }
}

package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.model.messageData.Message;
import com.uber.app.team23.AirRide.model.messageData.MessageType;
import com.uber.app.team23.AirRide.model.messageData.Note;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController @RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/user/{id}/ride")
    public ResponseEntity<UserPaginatedDTO> getUserRidesPaginated(
            @PathVariable Integer id, @RequestParam int page, @RequestParam int size, @RequestParam String sort,
            @RequestParam String from, @RequestParam String to) {

        return new ResponseEntity<>(new UserPaginatedDTO(), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/user")
    public ResponseEntity<UserPaginatedDTO> getUserPaginated(@RequestParam int page, @RequestParam int size) {
        Passenger p = new Passenger((long) 10, "Ime", "Prezime", "Phhoto", "123", "email", "adr", null, false, false, null, null);
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO(p));
        return new ResponseEntity<>(new UserPaginatedDTO(users), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/login")
    public ResponseEntity<TokensDTO> userLogin(@RequestBody LoginDTO loginParams){
        return new ResponseEntity<>(new TokensDTO( "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVC", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVC"),
                HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}/message", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> getAllMessages(@PathVariable Integer id) {
        MessageDTO msgDTO = new MessageDTO();

        Message msg = new Message((long) 123, (long) 456, "Text", LocalDateTime.now(), MessageType.RIDE, (long) 10, (long) 123);

        msgDTO.addMessageToList(msg);
        msgDTO.setTotalCount(msgDTO.getMessageList().size());

        return new ResponseEntity<>(msgDTO, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/user/{id}/message")
    public ResponseEntity<Message> sendMessage(@PathVariable Integer id, @RequestBody Message msg) {
        Message msgResponse = new Message();
        msgResponse.setId((long) 1);
        msgResponse.setSendTime(LocalDateTime.now());
        msgResponse.setSenderId((long) id);
        msgResponse.setReceiverId(msg.getReceiverId());
        msgResponse.setMessage(msg.getMessage());
        msgResponse.setType(msg.getType());
        msgResponse.setRideId(msg.getRideId());

        return new ResponseEntity<>(msgResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/user/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/user/{id}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/user/{id}/note")
    public ResponseEntity<Note> createNote(@PathVariable Integer id) {
        return new ResponseEntity<>(new Note((long) 123, LocalDateTime.now(), "Text"), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/user/{id}/note")
    public ResponseEntity<NoteDTO> getAllNotesForUser(@PathVariable Integer id, @RequestParam int page, @RequestParam int size) {
        NoteDTO noteDTO = new NoteDTO();

        Note note = new Note((long) page, LocalDateTime.now(), "Text");

        noteDTO.addNote(note);
        noteDTO.setTotalCount(noteDTO.getNoteLi().size());

        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }
}

package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.messageData.Message;
import com.uber.app.team23.AirRide.model.messageData.MessageType;
import com.uber.app.team23.AirRide.model.messageData.Note;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Base64;

@RestController @RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/user/{id}/ride")
    public ResponseEntity<UserPaginatedDTO> getUserRidesPaginated(
            @PathVariable Integer id, @RequestParam int page, @RequestParam int size, @RequestParam String sort,
            @RequestParam String from, @RequestParam String to) {

        return new ResponseEntity<>(new UserPaginatedDTO(), HttpStatus.OK);
    }

    @GetMapping(value = "/user")
    public ResponseEntity<UserPaginatedDTO> getUserPaginated(Pageable page) {

        return new ResponseEntity<>(new UserPaginatedDTO(), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserDTO> getUserData(@PathVariable Long id){
        User u = userService.findById(id);

        System.err.println("\n\nFROM REQUEST");
        System.err.println(Base64.getEncoder().encodeToString(u.getProfilePicture()));
        if (u == null) {
            throw new EntityNotFoundException("User With This ID Does Not Exist");
        } else {
            return new ResponseEntity<>(new UserDTO(u), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/user/{id}/message", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> getAllMessages(@PathVariable Integer id) {
        MessageDTO msgDTO = new MessageDTO();
        Message msg = new Message((long) 123, new Passenger(), new Driver(),"text", LocalDateTime.now(), MessageType.RIDE, new Ride());

        msgDTO.addMessageToList(msg);
        msgDTO.setTotalCount(msgDTO.getResults().size());

        return new ResponseEntity<>(msgDTO, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/user/{id}/message")
    public ResponseEntity<Message> sendMessage(@PathVariable Integer id, @RequestBody Message msg) {
        Message msgResponse = new Message();
        msgResponse.setId((long) 1);
        msgResponse.setTimeOfSending(LocalDateTime.now());
        User u = new Passenger();
        u.setId((long) id);
        msgResponse.setSender(u);
        msgResponse.setReceiver(msg.getReceiver());
        msgResponse.setMessage(msg.getMessage());
        msgResponse.setType(msg.getType());
        msgResponse.setRide(msg.getRide());

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
        noteDTO.setTotalCount(noteDTO.getResults().size());

        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }
}
package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.mapper.DriverDTOMapper;
import com.uber.app.team23.AirRide.mapper.RideDTOMapper;
import com.uber.app.team23.AirRide.mapper.UserDTOMapper;
import com.uber.app.team23.AirRide.model.messageData.AdminNoteDTO;
import com.uber.app.team23.AirRide.model.messageData.Message;
import com.uber.app.team23.AirRide.model.messageData.MessageType;
import com.uber.app.team23.AirRide.model.messageData.Note;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController @RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private RideService rideService;

    @Autowired
    private NoteService noteService;

    @GetMapping(value = "/user/{id}/ride")
    public ResponseEntity<?> getUserRidesPaginated(@PathVariable Long id, Pageable pageable) {
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User does not exist!");
        } else {
            Page<Ride> rides = driverService.findAllRides(u, pageable);
            List<RideResponseDTO> response = rides.stream().map(RideDTOMapper::fromRideToDTO).collect(Collectors.toList());
            return new ResponseEntity<>(new RidePaginatedDTO(response), HttpStatus.OK);
        }
    }


    @GetMapping(value = "/user")
    public ResponseEntity<UserPaginatedDTO> getUserPaginated(Pageable page) {
        Page<User> users = userService.findAll(page);
        List<UserDTO> userDTOS = users.stream().map(UserDTOMapper::fromUserToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(new UserPaginatedDTO(userDTOS), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserDTO> getUserData(@PathVariable Long id){
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User With This ID Does Not Exist");
        } else {
            return new ResponseEntity<>(new UserDTO(u), HttpStatus.OK);
        }
    }

    @Transactional
    @GetMapping(value = "/user/{id}/message")
    public ResponseEntity<MessageDTO> getAllMessages(@PathVariable Long id) {
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User does not exist!");
        } else {
            List<Message> messages = messageService.findAllForUser(u);
            return new ResponseEntity<>(new MessageDTO(messages), HttpStatus.OK);
        }
    }

    @Transactional
    @GetMapping(value = "/user/{passengerId}/{driverId}/{rideId}/message")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesForUsersByRide(@PathVariable Long passengerId, @PathVariable Long driverId, @PathVariable Long rideId){
        User u1 = userService.findById(passengerId);
        User u2 = userService.findById(driverId);
        Ride r = rideService.findOne(rideId);
        if (u1 == null || u2 == null){
            throw new EntityNotFoundException("User does not exist");
        }
        return new ResponseEntity<>(messageService.findAllByUsersForRide(u1, u2, r), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}/ride-messages")
    public ResponseEntity<RideMessages> getMessagesForRides(@PathVariable Long id){
        return null;
    }

    @Transactional
    @PostMapping(value = "/user/{id}/message")
    public ResponseEntity<MessageResponseDTO> sendMessage(@PathVariable Long id, @RequestBody SendMessageDTO dto) {
        //TODO Take User From Token
        User u1 = userService.findById(4L);
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User does not exist");
        }
        Ride ride = driverService.findRideById(dto.getRideId());
        if (ride == null) {
            throw new EntityNotFoundException("Ride does not exist");
        }
        Message msg = new Message(dto, ride, u, u1);
        msg = messageService.save(msg);
        MessageResponseDTO response = new MessageResponseDTO(msg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/user/{id}/block")
    public ResponseEntity<String > blockUser(@PathVariable Long id) {
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User does not exist!");
        } else if (u.isBlocked()) {
            throw new BadRequestException("User is already blocked");
        } else {
            u.setBlocked(true);
            userService.save(u);
            return new ResponseEntity<>("User is successfully blocked", HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping(value = "/user/{id}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable Long id) {
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User does not exist!");
        } else if (!u.isBlocked()) {
            throw new BadRequestException("User is not blocked");
        } else {
            u.setBlocked(false);
            userService.save(u);
            return new ResponseEntity<>("User is successfully unblocked", HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = "/user/{id}/note")
    public ResponseEntity<Note> createNote(@PathVariable Long id, @RequestBody AdminNoteDTO dto) {
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User does not exist");
        }
        Note note = new Note(dto, id);
        note = noteService.save(note);
        return new ResponseEntity<>(note, HttpStatus.OK);
    }


    @GetMapping(value = "/user/{id}/note")
    public ResponseEntity<NoteDTO> getAllNotesForUser(@PathVariable Long id, Pageable page) {
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User does not exist");
        }
        Page<Note> notes = noteService.findAll(id, page);
        List<Note> noteList = notes.stream().toList();
        return new ResponseEntity<>(new NoteDTO(noteList), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}/resetPassword")
    public ResponseEntity<?> resetPassword(@PathVariable Long id) {
        User u = userService.findById(id);
        if (u == null){
            throw new EntityNotFoundException("User does not exist!");
        } else {
            // TODO SEND EMAIL WITH UNIQUE CODE
            return new ResponseEntity<>("Email with reset code has been sent", HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping(value = "/user/{id}/resetPassword")
    public ResponseEntity<?> resetPasswordWithCode(@PathVariable Long id, @RequestBody ChangePasswordDTO dto) {
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User does not exist!");
        } else {
            // TODO CHECK isCodeValid AND UPDATE PASSWORD
            return new ResponseEntity<>("Password successfully changed!", HttpStatus.NO_CONTENT);
        }
    }
}
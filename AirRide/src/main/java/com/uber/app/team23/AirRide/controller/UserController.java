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
import com.uber.app.team23.AirRide.model.users.PasswordResetData;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.service.*;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    private WebSocketController webSocketController;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
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

    @Transactional
    @GetMapping(value = "user/{id}/rideCount")
    @PreAuthorize("hasAnyAuthority('ROLE_DRIVER', 'ROLE_USER')")
    public ResponseEntity<?> getRidesCount(@PathVariable Long id) {
        User u = userService.findById(id);
        JSONObject resp = new JSONObject();
        if (u == null) {
            throw new EntityNotFoundException("User does not exist");
        } else {
            if (u.getRole().get(0).getName().equals("ROLE_DRIVER")){
                return new ResponseEntity<>(resp.put("count", rideService.countForDriver(u)).toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(resp.put("count", rideService.countForPsngr(u)).toString(), HttpStatus.OK);
            }
        }
    }

    @GetMapping(value = "/user/exist/{email}")
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_DRIVER')")
    public ResponseEntity<?> doesUserExist(@PathVariable String email) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = userService.findByEmail(email);
        JSONObject resp = new JSONObject();
        if (u == null) {
            throw new EntityNotFoundException("User With This Email Does Not Exist");
        } else if (user.getEmail().equals(email)){
            return new ResponseEntity<>(resp.put("message", "You Cannot Invite Yourself").toString(), HttpStatus.NOT_FOUND);
        } else if (userService.isPassenger(u)) {
            return new ResponseEntity<>(resp.put("message", "User With This Email Does Exist").toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(resp.put("message", "You Cannot Invite Driver/Admin To ride").toString(), HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/user")
    public ResponseEntity<UserPaginatedDTO> getUserPaginated(Pageable page) {
        Page<User> users = userService.findAll(page);
        List<UserDTO> userDTOS = users.stream().map(UserDTOMapper::fromUserToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(new UserPaginatedDTO(userDTOS), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_DRIVER')")
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
    public ResponseEntity<List<MessageResponseDTO>> getAllMessages(@PathVariable Long id) {
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User does not exist!");
        } else {
            List<Message> messages = messageService.findAllForUser(u);
            List<MessageResponseDTO> messageDTOS = new ArrayList<>();
            for(Message m : messages){
                messageDTOS.add(new MessageResponseDTO(m));
            }
            return new ResponseEntity<>(messageDTOS, HttpStatus.OK);
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
    public ResponseEntity<MessageResponseDTO> sendMessage(@PathVariable Long id, @Valid @RequestBody SendMessageDTO dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u1 = userService.findById(user.getId());
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("Receiver does not exist");
        }
        Ride ride = driverService.findRideById(dto.getRideId());
        if (ride == null) {
            throw new EntityNotFoundException("Ride does not exist");
        }
        Message msg = new Message(dto, ride, u, u1);
        msg = messageService.save(msg);
        MessageResponseDTO response = new MessageResponseDTO(msg);
        webSocketController.simpMessagingTemplate.convertAndSend("/message/"+response.getRide().toString()+"/"+id.toString(), response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/user/{id}/note")
    public ResponseEntity<Note> createNote(@PathVariable Long id,@Valid @RequestBody AdminNoteDTO dto) {
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User does not exist");
        }
        Note note = new Note(dto, id);
        note = noteService.save(note);
        return new ResponseEntity<>(note, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    @PutMapping(value = "/user/forgotPassword")
    @Transactional
    public ResponseEntity<?> resetPassword(@RequestBody UserShortDTO dto) {
        User u = userService.findByEmail(dto.getEmail());
        if (u == null){
            throw new EntityNotFoundException("User does not exist!");
        } else {
            userService.sendResetPwCode(u);
            JSONObject resp = new JSONObject();
            return new ResponseEntity<>(resp.put("message", "Email with reset code has been sent").toString(), HttpStatus.OK);
        }
    }

    @PutMapping(value = "/user/resetPassword")
    @Transactional
    public ResponseEntity<?> resetPasswordWithCode(@RequestBody PasswordResetData dto) {
        User u = userService.findByEmail(dto.getEmail());
        if (u == null) {
            throw new EntityNotFoundException("User does not exist!");
        } else {
            userService.resetPassword(dto);
            JSONObject resp = new JSONObject();
            return new ResponseEntity<>(resp.put("message", "Password successfully changed!").toString(), HttpStatus.OK);
        }
    }
}
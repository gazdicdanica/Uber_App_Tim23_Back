package com.uber.app.team23.AirRide.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.OnClose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class WebSocketController {

    @Autowired
    public SimpMessagingTemplate simpMessagingTemplate;


    @RequestMapping(value = "/sendMessageRest")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String>message){
        if (message.containsKey("message")){
            if (message.containsKey("toId") && message.get("toId") != null && !message.get("toId").equals("")) {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.get("toId"), message);
            } else {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher", message);
            }
            return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @MessageMapping("/send/message")
    public Map<String ,String> broadcastNotification(String message){
        Map<String, String> messageConverted = parseMessage(message);
        if (messageConverted != null) {
            if (messageConverted.containsKey("toId") && messageConverted.get("toId") != null
                    && !messageConverted.get("toId").equals("")) {
                this.simpMessagingTemplate.convertAndSend("/ride/" + messageConverted.get("toId"),
                        messageConverted);
            } else {
                this.simpMessagingTemplate.convertAndSend("/ride", messageConverted);
            }
        }

        return messageConverted;
    }

    private Map<String, String> parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> retVal;

        try {
            retVal = mapper.readValue(message, Map.class); // parsiranje JSON stringa
        } catch (IOException e) {
            retVal = null;
        }

        return retVal;
    }

}

package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.messageData.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class MessageDTO {
    private int totalCount;
    private List<Message> results = new ArrayList<>();
    public void addMessageToList(Message msg) {
        this.results.add(msg);
    }

    public MessageDTO(List<Message> messages) {
        this.results = messages;
        this.totalCount = messages.size();
    }
}

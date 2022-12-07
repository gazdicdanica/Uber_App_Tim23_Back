package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class UserPaginatedDTO {
    private int totalCount;
    private List<User> results = new ArrayList<>();

    public void addUser(User user) {
        results.add(user);
    }

    public UserPaginatedDTO(User user) {
        totalCount += 1;
        addUser(user);
    }
}

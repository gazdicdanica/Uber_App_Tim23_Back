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
    private List<UserDTO> results = new ArrayList<>();

    public UserPaginatedDTO(List<UserDTO> results){
        this.results = results;
        this.totalCount = results.size();
    }
}

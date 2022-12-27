package com.uber.app.team23.AirRide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class TokensDTO {
    private String accessToken;
    private Long expiresIn;
}

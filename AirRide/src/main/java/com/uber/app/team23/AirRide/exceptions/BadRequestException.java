package com.uber.app.team23.AirRide.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BadRequestException extends RuntimeException{
    private String message;

    public BadRequestException(String message){
        super(message);
        this.message = message;
    }
}

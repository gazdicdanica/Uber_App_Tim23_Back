package com.uber.app.team23.AirRide.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmailTakenException extends RuntimeException{
    private String message;

    public EmailTakenException(String message){
        super(message);
        this.message = message;
    }
}

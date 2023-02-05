package com.uber.app.team23.AirRide.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityNotFoundException extends RuntimeException{
    private String message;

    public EntityNotFoundException(String message){
        super(message);
        this.message = message;
    }
}

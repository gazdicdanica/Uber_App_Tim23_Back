package com.uber.app.team23.AirRide.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ValidationErrorsHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException e) throws JsonProcessingException {
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();

        for (ObjectError error : errorList) {
            sb.append(error.getDefaultMessage());
        }
        JSONObject json = new JSONObject();
        json.put("message", sb.toString());
        return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
    }
}

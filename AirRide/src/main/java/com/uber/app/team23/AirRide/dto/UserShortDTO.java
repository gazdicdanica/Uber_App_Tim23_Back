package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserShortDTO {
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private int id;
    @NotNull @NotEmpty
    @Email(message = "Email Not Valid", regexp = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    public UserShortDTO(User user){
        this.id = user.getId().intValue();
        this.email = user.getEmail();
    }
    public UserShortDTO(Passenger passenger) {
        this.id = passenger.getId().intValue();
        this.email = passenger.getEmail();
    }
}

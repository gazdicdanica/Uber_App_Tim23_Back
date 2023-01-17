package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserDTO {
    private Long id;
    @NotNull @NotEmpty
    @Size(min = 2, max = 20)
    private String name;
    @NotNull @NotEmpty
    @Size(min = 2, max = 30)
    private String surname;
    @NotNull @NotEmpty
    private String profilePicture;
    @NotNull @NotEmpty
    @NumberFormat
    @Size(min = 3, max = 15)
    private String telephoneNumber;
    @Email(message = "Email Not Valid", regexp = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    private String address;

    public UserDTO(User user){
        this(user.getId(), user.getName(), user.getSurname(), null,
                user.getTelephoneNumber(), user.getEmail(), user.getAddress());
        if(user.getProfilePicture() != null){
            this.setProfilePicture(Base64.getEncoder().encodeToString(user.getProfilePicture()));

        }
    }

    public UserDTO(Long id, Driver driver) {
        this(id, driver.getName(), driver.getSurname(), Base64.getEncoder().encodeToString(driver.getProfilePicture()),
                driver.getTelephoneNumber(), driver.getEmail(), driver.getAddress());
    }
}

package com.uber.app.team23.AirRide.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "Reset_Codes")
public class PasswordResetData {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private Long userId;

//    @JsonIgnore
    private String password;

//    @JsonIgnore
    private String email;

    @Column(name = "code")
    private String code;

    public PasswordResetData(Long id, String code) {
        this.userId = id;
        this.code = code;
    }
}

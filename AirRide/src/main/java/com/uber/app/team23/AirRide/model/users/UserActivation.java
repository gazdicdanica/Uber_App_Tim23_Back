package com.uber.app.team23.AirRide.model.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "user_activations")
public class UserActivation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long activationId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
    @Column(name = "creation_date_time") @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime creationDT;
    @Column(name = "lifespan") @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime lifespan;
}

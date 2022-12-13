package com.uber.app.team23.AirRide.model.users;

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
    @Column(name = "creation_date_time")
    public LocalDateTime creationDT;
    @Column(name = "lifespan")
    public LocalDateTime lifespan;
}

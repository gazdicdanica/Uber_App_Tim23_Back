package com.uber.app.team23.AirRide.model.users;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {
    @SequenceGenerator(name = "generatorUserId", sequenceName = "mySeqUser", initialValue = 1, allocationSize = 1)
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    @Column(name = "id")
    protected Long id;
    @Column(name = "name", nullable = false)
    protected String name;
    @Column(name = "last_name", nullable = false)
    protected String lastName;
    @Column(name = "profile_photo")
    protected String profilePhoto;
    @Column(name = "phone_number", unique = true, nullable = false)
    protected String phoneNumber;
    @Column(name = "email", unique = true, nullable = false)
    protected String email;
    @Column(name = "address")
    protected String address;
    @Column(name = "password", nullable = false)
    protected String password;
    @Column(name = "blocked")
    protected boolean blocked;
    @Column(name = "active")
    protected boolean active;

}

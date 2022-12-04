package com.uber.app.team23.AirRide.model.users;

import jakarta.persistence.*;


@Entity
public class Admin{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "userName", unique = true, nullable = false)
    public String userName;
    @Column(name = "password", nullable = false)
    public String password;
    @Column(name = "name")
    public String name;
    @Column(name = "lastName")
    public String lastName;
    @Column(name = "profilePhoto")
    public byte[] profilePhoto;
}

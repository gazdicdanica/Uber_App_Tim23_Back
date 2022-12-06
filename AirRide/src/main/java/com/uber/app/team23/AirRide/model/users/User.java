package com.uber.app.team23.AirRide.model.users;

//import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor //@Entity //@Table(name = "Users")
public abstract class User {
    //@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
//    @Column(name = "name", nullable = false)
    protected String name;
//    @Column(name = "lastName", nullable = false)
    protected String lastName;
//    @Column(name = "profilePhoto")
    protected String profilePhoto;
//    @Column(name = "phoneNum", unique = true, nullable = false)
    protected String phoneNumber;
//    @Column(name = "email", unique = true, nullable = false)
    protected String email;
//    @Column(name = "address")
    protected String address;
//    @Column(name = "password", nullable = false)
    protected String password;
//    @Column(name = "blockedStatus")
    protected boolean blocked;
//    @Column(name = "activeStatus")
    protected boolean active;

    public User(Long id, String name, String lastName, String profilePhoto, String phoneNumber, String email,
                String address, String password, boolean blocked, boolean active) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.profilePhoto = profilePhoto;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
        this.blocked = blocked;
        this.active = active;
    }
}

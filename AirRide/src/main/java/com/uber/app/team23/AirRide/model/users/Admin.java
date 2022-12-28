package com.uber.app.team23.AirRide.model.users;

//import jakarta.persistence.*;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@DiscriminatorValue("admin")
public class Admin extends User{
    @Column(name = "admin_username", unique = true)
    public String username;
////    @Column(name = "password", nullable = false)
//    public String password;
////    @Column(name = "name")
//    public String name;
////    @Column(name = "lastName")
//    public String lastName;
}

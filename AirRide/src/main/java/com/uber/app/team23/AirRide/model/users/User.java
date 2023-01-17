package com.uber.app.team23.AirRide.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @NotNull @NotEmpty
    @Size(min = 3, max = 15)
    @Column(name = "name")
    protected String name;

    @NotNull @NotEmpty
    @Size(min = 3, max = 15)
    @Column(name = "last_name")
    protected String surname;

    @Lob
    @NotNull @NotEmpty
    @Column(name = "profile_picture")
    protected byte[] profilePicture;

    @Column(name = "telephone_number", unique = true)
    @NotNull @NotEmpty
    @NumberFormat
    @Size(min = 3, max = 15)
    protected String telephoneNumber;

    @Column(name = "email", unique = true)
    @Size(min = 3, max = 25)
    @Email(message = "Email Not Valid", regexp = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    protected String email;

    @Column(name = "username", unique = true)
    protected String username;

    @NotNull @NotEmpty
    @Size(min = 3, max = 30)
    @Column(name = "address")
    protected String address;

    @NotNull @NotEmpty
    @Column(name = "password")
    protected String password;

    @Column(name = "blocked")
    protected boolean blocked;

    @Column(name = "active")
    protected boolean active;

    @Transient
    private String jwt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    protected List<Role> role;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role;
    }
    @JsonIgnore
    @Override
    public String getUsername() {
        return this.username;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

}
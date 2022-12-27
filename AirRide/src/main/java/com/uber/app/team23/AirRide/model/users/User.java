package com.uber.app.team23.AirRide.model.users;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User implements UserDetails {
    @SequenceGenerator(name = "generatorUserId", sequenceName = "mySeqUser", initialValue = 1, allocationSize = 1)
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    @Column(name = "id")
    protected Long id;

    @Column(name = "name")
    protected String name;

    @Column(name = "last_name")
    protected String lastName;

    @Column(name = "profile_photo")
    protected String profilePhoto;

    @Column(name = "phone_number", unique = true)
    protected String phoneNumber;

    @Column(name = "email", unique = true)
    protected String email;

    @Column(name = "address")
    protected String address;

    @Column(name = "password")
    protected String password;

    @Column(name = "blocked")
    protected boolean blocked;

    @Column(name = "active")
    protected boolean active;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
//    private List<Role> roles;
    @OneToOne
    protected Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

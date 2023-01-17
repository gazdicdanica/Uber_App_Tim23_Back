package com.uber.app.team23.AirRide.model.users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;

@Entity
@Table(name = "Role")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Role implements GrantedAuthority {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }

    public Role(String role) {
        this.name = role;
    }
}



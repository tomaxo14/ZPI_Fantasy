package com.example.ZPI.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "users")
public class User {

    @Id
    private String login;

    private String firstName;
    private String surname;
    private String password;
    private String email;

    private Integer team;
    @DBRef
    private Set<Role> roles = new HashSet<>();

    public User(String login, String email, String firstName, String surname, String password) {
        this.login = login;
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.team = null;
    }


    public void addRole(Role tempRole) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(tempRole);
        //tempRole.setUser(this.getLogin());
    }
}

package com.example.ZPI.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private int team;
    private Set<Role> roles;

    public User(String firstName, String surname, String password, String email) {
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.email = email;
    }


    public void addRole(Role tempRole) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(tempRole);
        //tempRole.setUser(this.getLogin());
    }
}

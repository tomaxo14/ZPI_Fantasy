package com.example.ZPI.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "roles")
public class Role {

    @Id
    private int roleId;

    private String name;

    private Set<User> users;

    public Role(String name) {
        this.name = name;
    }

    public void addUser(User tempUser) {
        if (users == null) {
            users = new HashSet<>();
        }
        users.add(tempUser);
        //tempUser.setRole(this.getRoleId());
    }
}

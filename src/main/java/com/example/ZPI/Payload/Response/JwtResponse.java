package com.example.ZPI.Payload.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String login;
    private String email;
    private String firstName;
    private String surname;
    private Integer team;
    private List<String> roles;

    public JwtResponse(String token, String login, String email, String firstName, String surname, Integer team, List<String> roles) {
        this.token = token;
        this.login = login;
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.team = team;
        this.roles = roles;
    }
}

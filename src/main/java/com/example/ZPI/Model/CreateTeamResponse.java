package com.example.ZPI.Model;

import com.example.ZPI.entities.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeamResponse {
    private String info;
    private Team team;

    public CreateTeamResponse(String info, Team team) {
        this.info = info;
        this.team = team;
    }

}

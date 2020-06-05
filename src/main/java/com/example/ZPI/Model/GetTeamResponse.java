package com.example.ZPI.Model;

import com.example.ZPI.entities.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTeamResponse {

    private Team team;
    private int ranking;

    public GetTeamResponse(Team team, int ranking) {
        this.team = team;
        this.ranking = ranking;
    }
}

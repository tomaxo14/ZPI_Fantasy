package com.example.ZPI.Model;

import com.example.ZPI.entities.Team;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class TeamAthletesResponse {

    private Team team;
    private Map<Integer, String> clubs;
    private int ranking;

    public TeamAthletesResponse(Team team, Map<Integer, String> clubs, int ranking) {
        this.team = team;
        this.clubs = clubs;
        this.ranking=ranking;
    }
}

package com.example.ZPI.Model;

import com.example.ZPI.entities.Team;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RankingResponse {

    private Team team;
    private int ranking;

    public RankingResponse(Team team,int ranking) {
        this.team = team;
        this.ranking = ranking;
    }
}

package com.example.ZPI.Model;

import com.example.ZPI.entities.Athlete;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Setter
@Getter
public class TeamAthletesResponse {

    private Set<Athlete>athletes;
    private boolean hasTeam;

    public TeamAthletesResponse(Set<Athlete> athletes, boolean hasTeam) {
        this.athletes = athletes;
        this.hasTeam = hasTeam;
    }
}

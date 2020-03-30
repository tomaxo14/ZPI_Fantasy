package com.example.ZPI.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "teams")
public class Team {

    @Id
    private int teamId;

    private String name;
    private float budget;

    private int user;
    private Set<Athlete> athletes;

    public Team(String name, float budget) {
        this.name = name;
        this.budget = budget;
    }

    public void addAthlete(Athlete tempAthlete) {
        if (athletes == null) {
            athletes = new HashSet<>();
        }
        athletes.add(tempAthlete);
        //tempAthlete.setTeam(this.getTeamId());
    }
}

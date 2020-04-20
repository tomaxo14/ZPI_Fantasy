package com.example.ZPI.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "teams")
public class Team {
    @Transient
    private final static float DEFAULT_BUDGET = 100;

    @Id
    private int teamId;

    private String name;
    private float budget;
    private int points;

    private int user;
    private Set<Athlete> athletes;

    public Team(String name) {
        this.name = name;
        this.budget = DEFAULT_BUDGET;
        this.athletes = new HashSet<>();
        this.points = 0;
    }

    public void addAthlete(Athlete tempAthlete) {
        if (athletes == null) {
            athletes = new HashSet<>();
        }
        athletes.add(tempAthlete);
        budget = budget - tempAthlete.getValue();
    }

    public void removeAthlete(Athlete tempAthlete){
        athletes.remove(tempAthlete);
        budget = budget + tempAthlete.getValue();
    }

    @Override
    public int hashCode() {
        return ((Integer)teamId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return teamId==((Team)obj).getTeamId();
    }
}

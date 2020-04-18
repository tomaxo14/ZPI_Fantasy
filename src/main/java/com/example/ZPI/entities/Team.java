package com.example.ZPI.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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
    }

    public void addAthlete(Athlete tempAthlete) {
        if (athletes == null) {
            athletes = new HashSet<>();
        }
        athletes.add(tempAthlete);
    }
}

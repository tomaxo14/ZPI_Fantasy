package com.example.ZPI.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection="athletes")
public class Athlete {
    public enum Category{senior, junior, obcokrajowiec};

    @Id
    private int athleteId;

    private String firstName;
    private String surname;
    private String nationality;
    private float value;
    private Category category;

    private int club;
    private Set<Performance> performances;
    private Set<Team> teams;

    public Athlete(String firstName, String surname, String nationality, float value, String category) {
        this.firstName = firstName;
        this.surname = surname;
        this.nationality = nationality;
        this.value = value;
        this.category = Category.valueOf(category);
    }

    public void addPerformance(Performance tempPerformance) {
        if (performances == null) {
            performances = new HashSet<>();
        }
        performances.add(tempPerformance);
        tempPerformance.setAthlete(this.getAthleteId());
    }

    public void addTeam(Team tempTeam) {
        if (teams == null) {
            teams = new HashSet<>();
        }
        teams.add(tempTeam);
        //tempTeam.setAthlete(this.getAthleteId());
    }

}

package com.example.ZPI.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

import static com.example.ZPI.entities.ETeamRole.*;
import static com.example.ZPI.entities.ETeamRole.REGULAR;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public Team(int teamId, String name) {
        this.teamId = teamId;
        this.name = name;
        this.budget = DEFAULT_BUDGET;
        this.athletes = new HashSet<>();
        this.points = 0;
    }

    public Team(String name) {
        this.name = name;
        this.budget = DEFAULT_BUDGET;
        this.athletes = new HashSet<>();
        this.points = 0;
    }

    public void reset() {
        this.budget = DEFAULT_BUDGET;
        this.athletes = new HashSet<>();
        this.points = 0;
    }

    public void addAthlete(Athlete tempAthlete) {
        if (athletes == null) {
            athletes = new HashSet<>();
        }

        int countRegular = 0;
        List<ETeamRole> emptySub = new ArrayList<>();
        emptySub.add(SUB1);
        emptySub.add(SUB2);
        emptySub.add(SUB3);
        for (Athlete athlete : athletes) {
            switch (athlete.getTeamRole()) {
                case REGULAR:
                case CAPTAIN:
                case VICE:
                    countRegular++;
                    break;
                case SUB1:
                    emptySub.remove(SUB1);
                    break;
                case SUB2:
                    emptySub.remove(SUB2);
                    break;
                case SUB3:
                    emptySub.remove(SUB3);
                    break;
            }
        }

        if (countRegular < 7) {
            tempAthlete.setTeamRole(REGULAR);
        } else if (!emptySub.isEmpty()) {
            tempAthlete.setTeamRole(emptySub.get(0));
        }
        athletes.add(tempAthlete);
        budget = budget - tempAthlete.getValue();
    }

    public void updateAthlete(Athlete athlete) {
        athletes.remove(athlete);
        athletes.add(athlete);
    }

    public void removeAthlete(Athlete tempAthlete) {
        athletes.remove(tempAthlete);
        budget = budget + tempAthlete.getValue();
    }

    @Override
    public int hashCode() {
        return ((Integer) teamId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return teamId == ((Team) obj).getTeamId();
    }
}

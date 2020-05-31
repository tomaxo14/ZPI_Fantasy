package com.example.ZPI.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.ZPI.entities.ETeamRole.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "teams")
public class Team {
    @Transient
    private final static float DEFAULT_BUDGET = 8;

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
        int countCategory = 0;
        int countJuniorRegular = 0;
        int countForeignerRegular = 0;

        List<ETeamRole> emptySub = new ArrayList<>();
        emptySub.add(SUB1);
        emptySub.add(SUB2);
        emptySub.add(SUB3);
        for (Athlete athlete : athletes) {
            if (athlete.getCategory() == tempAthlete.getCategory()) countCategory++;
            if (athlete.getCategory() == Athlete.Category.junior
                    && (athlete.getTeamRole() == CAPTAIN || athlete.getTeamRole() == VICE
                    || athlete.getTeamRole() == REGULAR)) countJuniorRegular++;
            if (athlete.getCategory() == Athlete.Category.obcokrajowiec
                    && (athlete.getTeamRole() == CAPTAIN || athlete.getTeamRole() == VICE
                    || athlete.getTeamRole() == REGULAR)) countForeignerRegular++;

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

        switch (tempAthlete.getCategory()) {
            case junior:
                if (countRegular < 7 && countJuniorRegular < 2) tempAthlete.setTeamRole(REGULAR);
                else tempAthlete.setTeamRole(SUB3);
                break;
            case obcokrajowiec:
                if (countRegular - countJuniorRegular < 5 && countCategory < 4
                        && countForeignerRegular < 3) tempAthlete.setTeamRole(REGULAR);
                else tempAthlete.setTeamRole(emptySub.get(0));
                break;
            case senior:
                if (countRegular - countJuniorRegular < 5 && countCategory < 5) tempAthlete.setTeamRole(REGULAR);
                else tempAthlete.setTeamRole(emptySub.get(0));
                break;
        }

        athletes.add(tempAthlete);
        budget = budget - tempAthlete.getValue();
    }

    public void updateAthlete(Athlete athlete) {
        ETeamRole athleteRole = getAthleteRole(athlete.getAthleteId());
        athletes.remove(athlete);
        athlete.setTeamRole(athleteRole);
        athletes.add(athlete);
        this.points = 0;
        athletes.forEach(athlete1 -> this.points += athlete1.getPoints());

    }

    private ETeamRole getAthleteRole(int athleteId) {
        for(Athlete athlete : athletes) {
            if(athlete.getAthleteId() == athleteId) return athlete.getTeamRole();
        }

        return null;
    }

    public void removeAthlete(Athlete tempAthlete) {
        athletes.remove(tempAthlete);
        budget = budget + tempAthlete.getValue();
    }

    public void removePerformances() {
        for(Athlete athlete : athletes) {
            athlete.setPerformances(new HashSet<>());
            athlete.setPoints(0);
        }
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

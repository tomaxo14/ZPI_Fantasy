package com.example.ZPI.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection="clubs")
public class Club {

    @Id
    private int clubId;

    private String name;
    private String city;

    private Set<Integer> athletes;
    private Set<Match> matches;

    public Club(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public void addAthlete(Athlete tempAthlete) {
        if (athletes == null) {
            athletes = new HashSet<>();
        }
        athletes.add(tempAthlete.getAthleteId());
        tempAthlete.setClub(this.getClubId());
    }

    public void addMatch(Match tempMatch) {
        if (matches == null) {
            matches = new HashSet<>();
        }
       matches.add(tempMatch);
        //tempMatch.setClub(this.getClubId());
    }

    @Override
    public String toString() {
        return "Club{" +
                "clubId=" + clubId +
                ", name='" + name + '\'' +
                '}';
    }
}

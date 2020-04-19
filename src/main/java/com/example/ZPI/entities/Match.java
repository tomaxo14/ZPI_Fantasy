package com.example.ZPI.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "matches")
public class Match {

    @Id
    private int matchId;

    private int matchWeek;
    private String date;
    private int hostResult;
    private int awayResult;
    private String venue;

    private Set<Integer> clubs;
    private Set<Integer> performances;

    public Match(int matchWeek, String date, int hostResult, int awayResult, String venue) {
        this.matchWeek = matchWeek;
        this.date = date;
        this.hostResult = hostResult;
        this.awayResult = awayResult;
        this.venue = venue;
    }

    public void addClub(Club tempClub) {
        if (clubs == null) {
            clubs = new HashSet<>();
        }
        clubs.add(tempClub.getClubId());
        //tempClub.setMatch(this.getMatchId());
    }

    public void addPerformance(Performance tempPerformance) {
        if (performances == null) {
            performances = new HashSet<>();
        }
        performances.add(tempPerformance.getPerformanceId());
        tempPerformance.setMatch(this.getMatchId());
    }
}

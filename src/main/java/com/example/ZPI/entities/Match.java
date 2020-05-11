package com.example.ZPI.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

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

    private Club[] clubs;
    private Set<Integer> performances;

    public Match(int matchWeek, String date, int hostResult, int awayResult) {
        this.matchWeek = matchWeek;
        this.date = date;
        this.hostResult = hostResult;
        this.awayResult = awayResult;
    }

    public void setClub(int index, Club tempClub) {
        if (clubs == null) {
            clubs = new Club[2];
        }
        clubs[index] = tempClub;
        //tempClub.setMatch(this.getMatchId());
    }

    public void addPerformance(Performance tempPerformance) {
        if (performances == null) {
            performances = new HashSet<>();
        }
        performances.add(tempPerformance.getPerformanceId());
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", matchWeek=" + matchWeek +
                ", date='" + date + '\'' +
                ", hostResult=" + hostResult +
                ", awayResult=" + awayResult +
                ", clubs=" + Arrays.toString(clubs) +
                ", performances=" + performances +
                '}';
    }
}

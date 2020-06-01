package com.example.ZPI.Model;

import com.example.ZPI.entities.Athlete;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StatisticsResponse {

    Athlete athlete;
    String club;
    double average;
    int points;
    int bonuses;
    int heats;
    int overall;
    int ranking;

    public StatisticsResponse(Athlete athlete, String club, double average, int points, int bonuses, int heats, int overall) {
        this.athlete = athlete;
        this.club = club;
        this.average = average;
        this.points = points;
        this.bonuses = bonuses;
        this.heats = heats;
        this.overall = overall;
        this.ranking=0;
    }
}

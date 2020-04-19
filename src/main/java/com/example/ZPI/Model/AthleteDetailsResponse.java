package com.example.ZPI.Model;

import com.example.ZPI.entities.Athlete;
import com.example.ZPI.entities.Match;
import com.example.ZPI.entities.Performance;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AthleteDetailsResponse {

    private Athlete athlete;
    private Match nextMatch;
    private Performance lastPerformance;

    public AthleteDetailsResponse(Athlete athlete, Match nextMatch, Performance lastPerformance) {
        this.athlete = athlete;
        this.nextMatch = nextMatch;
        this.lastPerformance = lastPerformance;
    }
}

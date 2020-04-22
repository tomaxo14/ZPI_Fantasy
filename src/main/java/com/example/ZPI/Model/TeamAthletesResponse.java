package com.example.ZPI.Model;

import com.example.ZPI.entities.Athlete;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Set;

@Setter
@Getter
public class TeamAthletesResponse {

    private ArrayList<Pair<Athlete, String>> athletes;
    private boolean hasTeam;

    public TeamAthletesResponse(ArrayList<Pair<Athlete, String>> athletes, boolean hasTeam) {
        this.athletes = athletes;
        this.hasTeam = hasTeam;
    }
}

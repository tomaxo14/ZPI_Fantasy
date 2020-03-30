package com.example.ZPI.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "performences")
public class Performance {

    @Id
    private int performanceId;

    private int points;
    private int bonuses;
    private int heats;

    private int match;
    private int athlete;

    public Performance(int points, int bonuses, int heats) {
        this.points = points;
        this.bonuses = bonuses;
        this.heats = heats;
    }
}

package com.example.ZPI.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "performances")
public class Performance {

    @Id
    private int performanceId;

    private String date;
    private int points;
    private int bonuses;
    private int heats;


    public Performance(int points, int bonuses, int heats) {
        this.points = points;
        this.bonuses = bonuses;
        this.heats = heats;
    }

    public Performance(String date, int points, int bonuses, int heats) {
        this.date = date;
        this.points = points;
        this.bonuses = bonuses;
        this.heats = heats;
    }

    @Override
    public String toString() {
        return "Performance{" +
                "performanceId=" + performanceId +
                ", date='" + date + '\'' +
                ", points=" + points +
                ", bonuses=" + bonuses +
                ", heats=" + heats +
                '}';
    }
}

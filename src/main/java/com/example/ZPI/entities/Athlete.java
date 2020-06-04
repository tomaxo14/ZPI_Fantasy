package com.example.ZPI.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "athletes")
public class Athlete {
    public enum Category {senior, junior, obcokrajowiec}

    @Id
    private int athleteId;

    private String firstName;
    private String surname;
    private String nationality;
    private float value;
    private Category category;
    private int points;

    private int club;
    private Set<Performance> performances;
    private ETeamRole teamRole;

    public Athlete(String firstName, String surname, String nationality, float value, String category) {
        this.firstName = firstName;
        this.surname = surname;
        this.nationality = nationality;
        this.value = value;
        this.category = Category.valueOf(category);
    }

    public boolean isSub() {
        return (this.teamRole == ETeamRole.SUB1
                || this.teamRole == ETeamRole.SUB2
                || this.teamRole == ETeamRole.SUB3);
    }

    public void addPerformance(Performance tempPerformance) {
        if (performances == null) {
            performances = new HashSet<>();
        }
        performances.add(tempPerformance);

        this.points = 0;

        performances.forEach(performance ->
                this.points += performance.getPoints() + performance.getBonuses());
    }

    public Performance getPeformanceByMatchWeek(int matchWeek) {
        for (Performance performance : performances) {
            if (performance.getMatchWeek() == matchWeek) return performance;
        }

        return null;
    }

    public int getPointsByMatchWeek(int matchWeek) {
        for (Performance performance : performances) {
            if (performance.getMatchWeek() == matchWeek)
                return (performance.getPoints() + performance.getBonuses());
        }

        return -1;
    }

    @Override
    public String toString() {
        return "Athlete{" +
                "athleteId=" + athleteId +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", nationality='" + nationality + '\'' +
                ", value=" + value +
                ", category=" + category +
                ", points=" + points +
                ", club=" + club +
                ", performances=" + performances +
                ", teamRole=" + teamRole +
                '}';
    }

    @Override
    public int hashCode() {
        return ((Integer) athleteId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return athleteId == ((Athlete) obj).athleteId;
    }
}

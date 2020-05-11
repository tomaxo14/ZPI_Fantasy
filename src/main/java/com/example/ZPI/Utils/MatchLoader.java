package com.example.ZPI.Utils;

import com.example.ZPI.Model.PreparedMatch;
import com.example.ZPI.Repository.ClubRepository;
import com.example.ZPI.entities.Club;
import com.example.ZPI.entities.Match;
import com.example.ZPI.entities.Performance;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.HashMap;

public class MatchLoader {

    // TODO change to MatchResponse (Match, List<Performance>)
    public static PreparedMatch loadMatch(String filepath) {
        File file = new File(filepath);
        Match match = new Match();
        HashMap<String, Performance> performances = new HashMap<>(); // key: athleteName, value: Performance
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            line = br.readLine();   // first empty
            line = br.readLine();   // matchWeek
            match.setMatchWeek(Integer.valueOf(line));
            String date = br.readLine();   // date
            match.setDate(date.replaceAll("\\s+", ""));
            line = br.readLine();   // host team
            match.setClub(0, new Club(line, ""));  // add club name - change in Service
            line = br.readLine();   // host result
            match.setHostResult(Integer.valueOf(line));
            line = br.readLine();   // away team
            match.setClub(1, new Club(line, ""));  // add club name - change in Service
            line = br.readLine();   // away result
            match.setAwayResult(Integer.valueOf(line));

            // read home performances
            br.readLine();  // read line 'home'
            line = br.readLine();  // first athlete
            while (!line.startsWith("away") && line != null) {
                String athlete = line;
                int heats = Integer.valueOf(br.readLine());
                int points = Integer.valueOf(br.readLine());
                int bonus = Integer.valueOf(br.readLine());
                Performance performance = new Performance(date, points, bonus, heats);
                performances.put(athlete, performance);
                line = br.readLine();
            }

            line = br.readLine(); // first athlete
            while (line != null && !line.isEmpty()) {
                String athlete = line;
                int heats = Integer.valueOf(br.readLine());
                int points = Integer.valueOf(br.readLine());
                int bonus = Integer.valueOf(br.readLine());
                Performance performance = new Performance(date, points, bonus, heats);
                performances.put(athlete, performance);
                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(match);
        System.out.println(performances);
        return new PreparedMatch(match, performances);
    }
}

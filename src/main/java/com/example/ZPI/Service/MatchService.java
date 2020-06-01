package com.example.ZPI.Service;

import com.example.ZPI.Model.PreparedMatch;
import com.example.ZPI.Repository.*;
import com.example.ZPI.Utils.MatchLoader;
import com.example.ZPI.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class MatchService {
    public static final int FAILED = -1;
    public static final int STATUS_OK = 200;
    public static final int CLUB_NOT_FOUND = 1;
    public static final int ATHLETE_NOT_FOUND = 2;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    AthleteRepository athleteRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    CounterService counterService;

    @Autowired
    PerformanceService performanceService;

    @Autowired
    TeamService teamService;

    public void removeClubMatches() {
        List<Match> matches = matchRepository.findAll();
        for (Match match : matches) {
            for (Club club : match.getClubs()) {
                Optional<Club> dbClub = clubRepository.findById(club.getClubId());
                dbClub.ifPresent(value -> {
                    value.setMatches(new HashSet<>());
                    clubRepository.update(dbClub.get());
                });
            }
        }
    }

    public int updateClubMatches() {
        int counter = 0;
        List<Match> matches = matchRepository.findAll();
        for (Match match : matches) {
            for (Club club : match.getClubs()) {
                Optional<Club> dbClub = clubRepository.findById(club.getClubId());
                if (dbClub.isPresent() && !dbClub.get().getMatches().contains(match)) {
                    dbClub.get().addMatch(match);
                    clubRepository.update(dbClub.get());
                    counter++;
                }
            }
        }

        return counter;
    }

    public int addMatches(String directoryPath) {
        File directory = new File(directoryPath);
        int matches = 0;
        for (File fileEntry : directory.listFiles()) {
            int result = addMatch(fileEntry.getAbsolutePath());
            if (result != STATUS_OK) return FAILED;
            matches++;
        }

        return matches;
    }

    public int addMatch(String filepath) {
        System.out.println(" - - MatchService.addMatch() - - ");
        PreparedMatch preparedMatch = MatchLoader.loadMatch(filepath);
        Match match = preparedMatch.getMatch();
        Club[] matchClubs = match.getClubs();
        Club[] realClubs = new Club[2];
        for (int i = 0; i < matchClubs.length; i++) {
            Optional<Club> realClub = clubRepository.findByNameContains(matchClubs[i].getName());
            if (realClub.isPresent()) {
                realClubs[i] = realClub.get();
            } else {
                return CLUB_NOT_FOUND;
            }
        }

        match.setClubs(realClubs);

        List<Athlete> athletes = new ArrayList<>();
        List<Performance> performances = new ArrayList<>();

        for (Map.Entry<String, Performance> entry : preparedMatch.getPerformances().entrySet()) {
            String[] name = entry.getKey().split(" ");
            Optional<Athlete> athleteOpt = athleteRepository.findByFirstNameContainsAndSurnameContains(name[0], name[1]);
            if (athleteOpt.isPresent()) {
                Athlete athlete = athleteOpt.get();
                Performance performance = entry.getValue();
                performances.add(performance);
                athlete.addPerformance(performance);
                athleteRepository.update(athlete);
                athletes.add(athlete);
            } else {
                System.out.println("Nie znaleziono zawodnika: " + entry.getKey());
                return ATHLETE_NOT_FOUND;
            }
        }

        System.out.println("Match to add:");
        System.out.println(match);
        System.out.println("Performances to add:");
        System.out.println(performances);
        System.out.println("Athletes to update:");
        System.out.println(athletes);

//         add performances
        for (int i = 0; i < performances.size(); i++) {
            Performance addedPerformance = performanceService.addPerformance(performances.get(i));
            athletes.get(i).addPerformance(addedPerformance);
            athleteRepository.update(athletes.get(i));
            match.addPerformance(addedPerformance);
        }

        match.setMatchId(counterService.getNextId("match"));
        matchRepository.save(match);

        for (Club club : match.getClubs()) {
            Optional<Club> dbClub = clubRepository.findById(club.getClubId());
            if (dbClub.isPresent()) {
                dbClub.get().addMatch(match);
                clubRepository.update(dbClub.get());
            }
        }

        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            for (Athlete athlete : athletes) {
                if (team.getAthletes().contains(athlete)) {
                    team.updateAthlete(athlete);
                    teamRepository.update(team);
                }
            }
        }

        return STATUS_OK;
    }

    public List<Match> matchWeekResults(int matchWeek) {
        return matchRepository.findAllByMatchWeek(matchWeek);
    }

    public void removeMatchesAndPerformances() {

        //mecze w klubach
        List<Match> matches = matchRepository.findAll();
        for (Match match : matches) {
            for (Club club : match.getClubs()) {
                Optional<Club> dbClub = clubRepository.findById(club.getClubId());
                dbClub.ifPresent(value -> {
                    value.setMatches(new HashSet<>());
                    clubRepository.update(dbClub.get());
                });
            }
        }

        //performancy i punkty w zawodnikach
        List<Athlete> athletes = athleteRepository.findAll();
        for (Athlete athlete : athletes) {
            if (athlete.getPerformances() != null) {
                athlete.setPerformances(new HashSet<>());
                athlete.setPoints(0);
                athleteRepository.update(athlete);
            }
        }

        //usuwanie performance u zawodnikow w teamach
        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            team.removePerformances();
            teamRepository.update(team);
        }

        //usuwanie meczów
        for (int i = 0; i < matches.size(); i++) {
            matchRepository.delete(matches.get(i));
        }

        List<Performance> performances = performanceRepository.findAll();

        //usuwanie performancow
        for (int i = 0; i < performances.size(); i++) {
            performanceRepository.delete(performances.get(i));
        }

    }
}

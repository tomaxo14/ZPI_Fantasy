package com.example.ZPI.Service;

import com.example.ZPI.Model.AthleteDetailsResponse;
import com.example.ZPI.Model.TeamAthletesResponse;
import com.example.ZPI.Repository.*;
import com.example.ZPI.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AthleteService {

    @Autowired
    AthleteRepository athleteRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    UserService userService;

    public List<Athlete> getAllAthletes() {
        return athleteRepository.findAll();
    }

    public TeamAthletesResponse getTeamAthletes(String username) {

        boolean hasTeam = true;

        Optional<User> userOpt = userService.getUser(username);
        User user = userOpt.get();

        Team team;
        if(user.getTeam()==null){
            hasTeam=false;
            return new TeamAthletesResponse(new HashSet<>() , hasTeam);
        }else {
            Optional<Team> teamOpt = teamRepository.findById(user.getTeam());
            team = teamOpt.get();
            return new TeamAthletesResponse(team.getAthletes(), hasTeam);
        }
    }

    public Optional<Athlete> getAthlete(int id) {
        return athleteRepository.findById(id);
    }

    public AthleteDetailsResponse getAthleteDetails(int athleteId) throws ParseException {

        Optional<Athlete> athleteOpt = athleteRepository.findById(athleteId);
        Athlete athlete = athleteOpt.get();

        Optional<Club> clubOpt = clubRepository.findById(athlete.getClub());
        Club club = clubOpt.get();
        String clubName = club.getName();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //jesli bedziemy przechowywac referencje zamiast całych meczow w clubie
//        Set<Integer> matchesId = club.getMatches();
//        Set<Match> matches = new HashSet<>();
//
//        for(Integer id : matchesId){
//            Optional<Match> matchOpt = matchRepository.findById(id);
//            Match match = matchOpt.get();
//            matches.add(match);
//        }

        Set<Match>matches = club.getMatches();

        String actualDate = dateFormat.format(new Date());
        String closestDate = dateFormat.format(new Date(Long.MAX_VALUE));
        Match nextMatch=null;

        for (Match match : matches){
            if(match.getDate().compareTo(actualDate)>=0 && match.getDate().compareTo(closestDate)<0){
                closestDate = match.getDate();
                nextMatch = match;
            }
        }

        Set<Integer>performancesId = athlete.getPerformances();
        Set<Performance>performances = new HashSet<>();

        for(int id : performancesId){
            Optional<Performance> performanceOpt = performanceRepository.findById(id);
            Performance performance = performanceOpt.get();
            performances.add(performance);
        }

        String lastPerformanceDate = "1900-01-01";
        System.out.println(lastPerformanceDate);
        Performance lastPerformance=null;
        for(Performance performance : performances){
            Optional<Match> matchOpt = matchRepository.findById(performance.getMatch());
            Match match = matchOpt.get();
            if(match.getDate().compareTo(actualDate)<=0 && match.getDate().compareTo(lastPerformanceDate)>0){
                lastPerformanceDate = match.getDate();
                lastPerformance = performance;
            }
        }

        return new AthleteDetailsResponse(athlete, nextMatch, lastPerformance, clubName);
    }
}

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

        //ArrayList<Pair<Athlete, String>> pairList = new ArrayList<>();
        List<Athlete>athleteList = athleteRepository.findAll();

        //pobieranie nazwy klubu każdego sportowca
//        for(Athlete athlete : athleteList){
//            Optional<Club>clubOpt = clubRepository.findById(athlete.getClub());
//            Club club = clubOpt.get();
//            Pair<Athlete, String> pair = new Pair<>(athlete, club.getName());
//            pairList.add(pair);
//        }

        return athleteList;
    }

    public TeamAthletesResponse getTeamAthletes(String username) {

        boolean hasTeam = true;

        Optional<User> userOpt = userService.getUser(username);
        User user = userOpt.get();

        //ArrayList<Pair<Athlete, String>> pairList = new ArrayList<>();


        Team team;
        if(user.getTeam()==null){
            hasTeam=false;
            return new TeamAthletesResponse(new HashSet<>() , hasTeam);
        }else {
            Optional<Team> teamOpt = teamRepository.findById(user.getTeam());
            team = teamOpt.get();
            //Set<Athlete> athletes = team.getAthletes();

            //pobieranie nazwy klubu każdego sportowca
//            for(Athlete athlete : athletes){
//                Optional<Club>clubOpt = clubRepository.findById(athlete.getClub());
//                Club club = clubOpt.get();
//                Pair<Athlete, String> pair = new Pair<>(athlete, club.getName());
//                pairList.add(pair);
//            }
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

        Set<Performance>performances = athlete.getPerformances();

        String lastPerformanceDate = "1900-01-01";
        System.out.println(lastPerformanceDate);
        Performance lastPerformance=null;
        // TODO do sprawdzenia
        for(Performance performance : performances){
            if(performance.getDate().compareTo(actualDate)<=0 && performance.getDate().compareTo(lastPerformanceDate)>0){
                lastPerformanceDate = performance.getDate();
                lastPerformance = performance;
            }
        }

        return new AthleteDetailsResponse(athlete, nextMatch, lastPerformance, clubName);
    }
}

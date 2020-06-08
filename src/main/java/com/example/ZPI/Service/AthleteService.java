package com.example.ZPI.Service;

import com.example.ZPI.Model.AthleteDetailsResponse;
import com.example.ZPI.Model.StatisticsResponse;
import com.example.ZPI.Model.TeamAthletesResponse;
import com.example.ZPI.Repository.*;
import com.example.ZPI.Utils.MapUtil;
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

    @Autowired
    TeamService teamService;

    @Autowired
    CounterService counterService;

    public List<Athlete> getAllAthletes() {
        List<Athlete> athleteList = athleteRepository.findAll();
        return athleteList;
    }

    public TeamAthletesResponse getTeamAthletes(String username) {

        Optional<User> userOpt = userService.getUser(username);
        User user = userOpt.get();

        Team team;
        if (user.getTeam() == null) {
            return new TeamAthletesResponse();
        } else {
            Optional<Team> teamOpt = teamRepository.findById(user.getTeam());
            team = teamOpt.get();
            Map<Integer, String> clubNames = new HashMap<>();

            //pobieranie nazwy klubu każdego sportowca
            Set<Athlete> athletes = team.getAthletes();
            for (Athlete athlete : athletes) {
                Optional<Club> clubOpt = clubRepository.findById(athlete.getClub());
                Club club = clubOpt.get();
                clubNames.put(club.getClubId(), club.getName());
            }

            List<Team> teams = teamRepository.findAll();
            Map<Integer, Integer> teamsAndPoints = new HashMap<>();
            for (Team teamInTeams : teams) {
                teamsAndPoints.put(teamInTeams.getTeamId(), teamInTeams.getPoints());
            }
            teamsAndPoints = MapUtil.sortByValue(teamsAndPoints);

            int ranking = 0;

            for (Map.Entry<Integer, Integer> entry : teamsAndPoints.entrySet()) {
                ranking++;
                if (entry.getKey() == team.getTeamId()) {
                    break;
                }
            }

            int matchWeek = counterService.getCurrentValue("matchweek");
            int points = teamService.getTeamPointsByMatchWeek(team, matchWeek);

            return new TeamAthletesResponse(team, clubNames, ranking, matchWeek, points);
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

        Set<Match> matches = club.getMatches();

        //aktualna data
//        String actualDate = dateFormat.format(new Date());
        String actualDate = "2019-04-10";
        String closestDate = dateFormat.format(new Date(Long.MAX_VALUE));
        Match nextMatch = null;

        for (Match match : matches) {
            if (match.getDate().compareTo(actualDate) >= 0 && match.getDate().compareTo(closestDate) < 0) {
                closestDate = match.getDate();
                nextMatch = match;
            }
        }

        Set<Performance> performances = athlete.getPerformances();

        String lastPerformanceDate = "1900-01-01";
        Performance lastPerformance = null;
        // TODO do sprawdzenia
        for (Performance performance : performances) {
            if (performance.getDate().compareTo(actualDate) <= 0 && performance.getDate().compareTo(lastPerformanceDate) > 0) {
                lastPerformanceDate = performance.getDate();
                lastPerformance = performance;
            }
        }

        return new AthleteDetailsResponse(athlete, nextMatch, lastPerformance, clubName);
    }

    public List<StatisticsResponse> statistics() {
        List<Athlete> athletes = athleteRepository.findAll();
        List<StatisticsResponse> resultList = new ArrayList<>();
        for (Athlete athlete : athletes) {
            Optional<Club> optClub = clubRepository.findById(athlete.getClub());
            Club club = optClub.get();
            String clubName = club.getName();
            int overallPoints = athlete.getPoints();
            int points = 0;
            int bonuses = 0;
            int heats = 0;
            for (Performance performance : athlete.getPerformances()) {
                points += performance.getPoints();
                bonuses += performance.getBonuses();
                heats += performance.getHeats();
            }
            double average = 0;
            if (heats != 0) {
                average = (double) overallPoints / heats;
            }
            resultList.add(new StatisticsResponse(athlete, clubName, average, points, bonuses, heats, overallPoints));
        }

        Collections.sort(resultList, Comparator.comparing(p -> -p.getOverall()));
        int ranking = 0;
        for (StatisticsResponse sr : resultList) {
            ranking++;
            sr.setRanking(ranking);
        }

        return resultList;
    }
}

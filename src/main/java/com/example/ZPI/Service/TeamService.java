package com.example.ZPI.Service;

import com.example.ZPI.Model.RankingResponse;
import com.example.ZPI.Repository.TeamRepository;
import com.example.ZPI.Utils.MapUtil;
import com.example.ZPI.entities.*;
//import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TeamService {
    private static final int MAX_ATHLETES_NUMBER = 10;
    public static final int FAILED = -1;
    public static final int STATUS_OK = 200;
    public static final int USER_NOT_FOUND = 1;
    public static final int USER_ALREADY_OWNS_A_TEAM = 2;
    public static final int TEAM_ALREADY_EXISTS = 3;
    public static final int ATHLETE_NOT_FOUND = 4;
    public static final int NO_TEAM_OWNED_BY_USER = 5;
    public static final int TEAM_NOT_FOUND = 6;
    public static final int NO_BUDGET = 7;
    public static final int CANNOT_BUY_JUNIOR = 8;
    public static final int CANNOT_BUY_SENIOR = 9;
    public static final int CANNOT_BUY_FOREIGNER = 10;
    public static final int TEAM_IS_FULL = 11;
    public static final int ATHLETE_IS_IN_A_TEAM = 12;
    public static final int ATHLETE_IS_NOT_IN_A_TEAM = 13;
    public static final int BAD_ROLE_NUMBER = 14;
    public static final int CANNOT_CHANGE_SENIOR_FOREIGNER = 15;
    public static final int CANNOT_CHANGE_SENIOR_JUNIOR = 16;
    public static final int CANNOT_CHANGE_FOREIGNER_JUNIOR = 17;
    public static final int CANNOT_CHANGE_JUNIOR_SENIOR = 18;
    public static final int CANNOT_CHANGE_JUNIOR_FOREIGNER = 19;
    public static final int CANNOT_CHANGE_SUB_SUB3 = 20;
    public static final int CANNOT_MAKE_SUB_CAPTAIN = 21;
    public static final int CANNOT_MAKE_SUB_VICE = 22;
    public static final int CANNOT_BUY_CLUB = 23;
    public static final int UPDATE_PREVIOUS_MATCHWEEK = 24;
    public static final int MATCHWEEK_ALREADY_UPDATED = 25;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserService userService;

    @Autowired
    AthleteService athleteService;

    @Autowired
    CounterService counterService;

    public int createTeam(String username, String teamName) {

        Optional<User> userOpt = userService.getUser(username);
        Optional<Team> teamOpt = teamRepository.findByNameEquals(teamName);

        if (!userOpt.isPresent()) return USER_NOT_FOUND;
        if (teamOpt.isPresent()) return TEAM_ALREADY_EXISTS;

        User user = userOpt.get();
        if (user.getTeam() != null) return USER_ALREADY_OWNS_A_TEAM;

        Team team = new Team(counterService.getNextId("team"), teamName);
        team.setUser(user.getLogin());
        team = teamRepository.save(team);
        userService.updateTeam(user, team.getTeamId());
        return STATUS_OK;
    }

    public int resetTeam(String username) {
        Optional<User> userOpt = userService.getUser(username);
        if (!userOpt.isPresent()) return USER_NOT_FOUND;
        User user = userOpt.get();

        if (user.getTeam() == null) return NO_TEAM_OWNED_BY_USER;
        Optional<Team> teamOpt = teamRepository.findById(user.getTeam());
        if (!teamOpt.isPresent()) return TEAM_NOT_FOUND;
        Team team = teamOpt.get();
        team.reset();

        if (teamRepository.update(team)) return STATUS_OK;

        return FAILED;
    }

    public int deleteTeam(int teamId) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (!teamOpt.isPresent()) return TEAM_NOT_FOUND;
        Team team = teamOpt.get();

        teamRepository.delete(team);
        return STATUS_OK;
    }

    public int buyAthlete(String username, int athleteId) {
        Optional<User> userOpt = userService.getUser(username);
        Optional<Athlete> athleteOpt = athleteService.getAthlete(athleteId);

        if (!userOpt.isPresent()) return USER_NOT_FOUND;
        if (!athleteOpt.isPresent()) return ATHLETE_NOT_FOUND;

        Athlete athlete = athleteOpt.get();
        User user = userOpt.get();

        if (user.getTeam() == null) return NO_TEAM_OWNED_BY_USER;

        Optional<Team> teamOpt = teamRepository.findById(user.getTeam());

        if (!teamOpt.isPresent()) return TEAM_NOT_FOUND;

        Team team = teamOpt.get();

        if (team.getAthletes() != null && team.getAthletes().contains(athlete)) return ATHLETE_IS_IN_A_TEAM;
        if (team.getBudget() < athlete.getValue()) return NO_BUDGET;

        int countJunior = 0;
        int countSenior = 0;
        int countForeigner = 0;
        int countClub = 0;

        if (team.getAthletes() != null) {
            for (Athlete teamAthlete : team.getAthletes()) {
                switch (teamAthlete.getCategory()) {
                    case junior:
                        countJunior++;
                        break;
                    case senior:
                        countSenior++;
                        break;
                    case obcokrajowiec:
                        countForeigner++;
                        break;
                }

                if (teamAthlete.getClub() == athlete.getClub()) countClub++;
            }
        }

        if (countJunior + countSenior + countForeigner == MAX_ATHLETES_NUMBER) return TEAM_IS_FULL;

        if (countClub == 3) return CANNOT_BUY_CLUB;

        switch (athlete.getCategory()) {
            case junior:
                if (countJunior == 3) return CANNOT_BUY_JUNIOR;
                break;
            case obcokrajowiec:
                if (countForeigner == 4 || countForeigner + countSenior == 7) return CANNOT_BUY_FOREIGNER;
                break;
            case senior:
                if (countSenior == 7 || countForeigner + countSenior == 7) return CANNOT_BUY_SENIOR;
                break;
        }


        team.addAthlete(athlete);
        if (teamRepository.update(team)) return STATUS_OK;

        return FAILED;
    }

    public int sellAthlete(String username, int athleteId) {

        Optional<User> userOpt = userService.getUser(username);
        Optional<Athlete> athleteOpt = athleteService.getAthlete(athleteId);

        if (!userOpt.isPresent()) return USER_NOT_FOUND;
        if (!athleteOpt.isPresent()) return ATHLETE_NOT_FOUND;

        Athlete athlete = athleteOpt.get();
        User user = userOpt.get();

        if (user.getTeam() == null) return NO_TEAM_OWNED_BY_USER;

        Optional<Team> teamOpt = teamRepository.findById(user.getTeam());

        if (!teamOpt.isPresent()) return TEAM_NOT_FOUND;

        Team team = teamOpt.get();

        if (!team.getAthletes().contains(athlete)) return ATHLETE_IS_NOT_IN_A_TEAM;

        team.removeAthlete(athlete);
        if (teamRepository.update(team)) return STATUS_OK;

        return FAILED;
    }

    public int setRole(String username, int athleteId, int role) {

        Optional<User> userOpt = userService.getUser(username);
        if (!userOpt.isPresent()) return USER_NOT_FOUND;
        User user = userOpt.get();

        if (user.getTeam() == null) return NO_TEAM_OWNED_BY_USER;
        Optional<Team> teamOpt = teamRepository.findById(user.getTeam());

        if (!teamOpt.isPresent()) return TEAM_NOT_FOUND;
        Team team = teamOpt.get();

        Optional<Athlete> athleteOpt = athleteService.getAthlete(athleteId);
        if (!athleteOpt.isPresent()) return ATHLETE_NOT_FOUND;
        Athlete athlete = athleteOpt.get();

        if (!team.getAthletes().contains(athlete)) return ATHLETE_IS_NOT_IN_A_TEAM;

        for (Athlete teamAthlete : team.getAthletes()) {
            if (teamAthlete.getAthleteId() == athleteId && (teamAthlete.getTeamRole() == ETeamRole.SUB1 || teamAthlete.getTeamRole() == ETeamRole.SUB2 || teamAthlete.getTeamRole() == ETeamRole.SUB3)) {
                if (role == 1) {
                    return CANNOT_MAKE_SUB_CAPTAIN;
                } else if (role == 2) {
                    return CANNOT_MAKE_SUB_VICE;
                }
            }
        }

        //Athlete athleteInTeam = team.getAthletes().;
        //team.getAthletes().remove(athlete);
        switch (role) {
            case 1:
                for (Athlete teamAthlete : team.getAthletes()) {
                    if (teamAthlete.getTeamRole() == ETeamRole.CAPTAIN) {
                        teamAthlete.setTeamRole(ETeamRole.REGULAR);
                    }
                }
                for (Athlete teamAthlete : team.getAthletes()) {
                    if (teamAthlete.getAthleteId() == athleteId) {
                        teamAthlete.setTeamRole(ETeamRole.CAPTAIN);
                    }
                }
                break;
            case 2:
                for (Athlete teamAthlete : team.getAthletes()) {
                    if (teamAthlete.getTeamRole() == ETeamRole.VICE) {
                        teamAthlete.setTeamRole(ETeamRole.REGULAR);
                    }
                }
                for (Athlete teamAthlete : team.getAthletes()) {
                    if (teamAthlete.getAthleteId() == athleteId) {
                        teamAthlete.setTeamRole(ETeamRole.VICE);
                    }
                }
                break;
            default:
                return BAD_ROLE_NUMBER;
        }
        //team.getAthletes().add(athlete);
        if (teamRepository.update(team)) return STATUS_OK;

        return FAILED;
    }

    public int setSub(String username, int regularId, int subId) {

        Optional<User> userOpt = userService.getUser(username);
        if (!userOpt.isPresent()) return USER_NOT_FOUND;
        User user = userOpt.get();

        if (user.getTeam() == null) return NO_TEAM_OWNED_BY_USER;
        Optional<Team> teamOpt = teamRepository.findById(user.getTeam());

        if (!teamOpt.isPresent()) return TEAM_NOT_FOUND;
        Team team = teamOpt.get();

        Optional<Athlete> regularOpt = athleteService.getAthlete(regularId);
        if (!regularOpt.isPresent()) return ATHLETE_NOT_FOUND;
        Athlete regular = null;

        Optional<Athlete> subOpt = athleteService.getAthlete(subId);
        if (!subOpt.isPresent()) return ATHLETE_NOT_FOUND;
        Athlete sub = null;

        for (Athlete teamAthlete : team.getAthletes()) {
            if (teamAthlete.getAthleteId() == regularId) {
                regular = teamAthlete;
            }
            if (teamAthlete.getAthleteId() == subId) {
                sub = teamAthlete;
            }
        }

        //sprawdzenie czy nie zamieniamy rezerwowych z rezerwowym juniorem
        if (((regular.getTeamRole() == ETeamRole.SUB1 || regular.getTeamRole() == ETeamRole.SUB2) && regular.getTeamRole() == ETeamRole.SUB3)
                || (regular.getTeamRole() == ETeamRole.SUB3 && (regular.getTeamRole() == ETeamRole.SUB1 || regular.getTeamRole() == ETeamRole.SUB2))) {
            return CANNOT_CHANGE_SUB_SUB3;
        }

        //liczba seniorów w pierwszym składzie
        int countSenior = 0;

        for (Athlete teamAthlete : team.getAthletes()) {
            if ((teamAthlete.getTeamRole() == ETeamRole.REGULAR || teamAthlete.getTeamRole() == ETeamRole.CAPTAIN || teamAthlete.getTeamRole() == ETeamRole.VICE) && teamAthlete.getCategory() == Athlete.Category.senior) {
                countSenior++;
            }
        }

        //sprawdzenie czy regular jest w pierwszym składzie czy zmieniamy rezerwowych miedzy sobą
        if (regular.getTeamRole() == ETeamRole.REGULAR || regular.getTeamRole() == ETeamRole.CAPTAIN || regular.getTeamRole() == ETeamRole.VICE) {
            //sprawdzenie warunków zmiany roli
            switch (regular.getCategory()) {
                case senior:
                    switch (sub.getCategory()) {
                        case obcokrajowiec:
                            if (countSenior <= 2) {
                                return CANNOT_CHANGE_SENIOR_FOREIGNER;
                            }
                            break;
                        case junior:
                            return CANNOT_CHANGE_SENIOR_JUNIOR;
                    }
                    break;
                case obcokrajowiec:
                    if (sub.getCategory() == Athlete.Category.junior) {
                        return CANNOT_CHANGE_FOREIGNER_JUNIOR;
                    }
                    break;
                case junior:
                    switch (sub.getCategory()) {
                        case senior:
                            return CANNOT_CHANGE_JUNIOR_SENIOR;
                        case obcokrajowiec:
                            return CANNOT_CHANGE_JUNIOR_FOREIGNER;
                    }
                    break;
            }
        }

        ETeamRole tempRegularRole = null;
        ETeamRole tempSubRole = null;

        //pobranie roli obu zawodników
        for (Athlete teamAthlete : team.getAthletes()) {
            if (teamAthlete.getAthleteId() == regularId) {
                tempRegularRole = teamAthlete.getTeamRole();
            }
            if (teamAthlete.getAthleteId() == subId) {
                tempSubRole = teamAthlete.getTeamRole();
            }
        }

        //ustalenie nowych ról
        for (Athlete teamAthlete : team.getAthletes()) {
            if (teamAthlete.getAthleteId() == regular.getAthleteId()) {
                teamAthlete.setTeamRole(tempSubRole);
            }
            if (teamAthlete.getAthleteId() == sub.getAthleteId()) {
                teamAthlete.setTeamRole(tempRegularRole);
            }
        }

        if (teamRepository.update(team)) return STATUS_OK;

        return FAILED;
    }

    public List<RankingResponse> ranking() {

        List<Team> teams = teamRepository.findAll();
//        List<Pair<Team, Integer>>teamsAndPoints = new ArrayList<>();
        Map<Team, Integer> teamsAndPoints = new HashMap<>();
        for (Team teamInTeams : teams) {
//            teamsAndPoints.add(new Pair<>(teamInTeams, teamInTeams.getPoints()));
            teamsAndPoints.put(teamInTeams, teamInTeams.getPoints());

        }
//        Collections.sort(teamsAndPoints, Comparator.comparing(p -> -p.getValue()));
        teamsAndPoints = MapUtil.sortByValue(teamsAndPoints);

        int ranking = 0;
        List<RankingResponse> resultList = new ArrayList<>();
        for (Map.Entry<Team, Integer> entry : teamsAndPoints.entrySet()) {
            ranking++;
            resultList.add(new RankingResponse(entry.getKey(), ranking));
        }
        return resultList;
    }

    public int updatePoints(int matchWeek) {
        int lastUpdate = counterService.getCurrentValue("matchweek");
        System.out.println("Last update: " + lastUpdate);
        if(matchWeek<=lastUpdate) {
            return MATCHWEEK_ALREADY_UPDATED;
        } else if(matchWeek>lastUpdate+1) {
            return UPDATE_PREVIOUS_MATCHWEEK;
        }

        List<Team> teams = teamRepository.findAll();
        teams.forEach(team -> updateTeamPoints(matchWeek, team));
        counterService.updateValue("matchweek", matchWeek);

        return STATUS_OK;
    }

    private void updateTeamPoints(int matchWeek, Team team) {
        Set<Athlete> athletesSet = team.getAthletes();
        Map<Athlete, Integer> regularAthletes = new LinkedHashMap<>();
        List<Athlete> subAthletes = new ArrayList<>();
        AtomicInteger regularForeignerCounter = new AtomicInteger();

        for (Athlete athlete : athletesSet) {
            Performance performance = athlete.getPeformanceByMatchWeek(matchWeek);
            if (athlete.getTeamRole() == ETeamRole.REGULAR
                    || athlete.getTeamRole() == ETeamRole.CAPTAIN
                    || athlete.getTeamRole() == ETeamRole.VICE) {
                if (athlete.getCategory() == Athlete.Category.obcokrajowiec) regularForeignerCounter.getAndIncrement();
                if (performance != null) {
                    regularAthletes.put(athlete, performance.getPoints() + performance.getBonuses());
                } else {
                    regularAthletes.put(athlete, -1);
                }
            } else {    // SUB
                subAthletes.add(athlete);
            }
        }

        List<Athlete> athletesToReplace = new ArrayList<>();
        regularAthletes.forEach((athlete, points) -> {
            if (points == -1) athletesToReplace.add(athlete);
        });

//        System.out.println("To replace: " + athletesToReplace.size());

//        int finalRegularForeignerCounter = regularForeignerCounter.get();
        athletesToReplace.forEach(athlete -> {
            regularAthletes.remove(athlete);
            if(athlete.getCategory()== Athlete.Category.obcokrajowiec) regularForeignerCounter.getAndDecrement();

            boolean foreignerAllowed = false;
            if (athlete.getCategory() == Athlete.Category.obcokrajowiec
                    || regularForeignerCounter.get() < 3) foreignerAllowed = true;
            Athlete sub = findSub(athlete, subAthletes, matchWeek, foreignerAllowed);
            if (sub != null) {
                regularAthletes.put(sub, sub.getPointsByMatchWeek(matchWeek));
                if(sub.getCategory()== Athlete.Category.obcokrajowiec) regularForeignerCounter.getAndIncrement();
                subAthletes.remove(sub);
            }
        });

        Athlete captain = null;
        for (Map.Entry<Athlete, Integer> entry : regularAthletes.entrySet()) {
            Athlete athlete = entry.getKey();
            if (athlete.getTeamRole() == ETeamRole.CAPTAIN)
                captain = athlete;
            else if (athlete.getTeamRole() == ETeamRole.VICE && captain == null) {
                captain = athlete;
            }
        }

        if (captain != null) regularAthletes.put(captain, regularAthletes.get(captain)*2);

        AtomicInteger teamPoints = new AtomicInteger(0);
//            System.out.println("REGULAR");
        regularAthletes.forEach((athlete, points) -> {
//                System.out.println("[" + athlete.getTeamRole() + "] " + athlete.getSurname() + ": " + points);
                teamPoints.addAndGet(points);
                });
//        System.out.println("SUB");
//        subAthletes.forEach(athlete ->
//                System.out.println("[" + athlete.getTeamRole() + "] " + athlete.getSurname() + ": "));

//        System.out.println("Team points: " + teamPoints.get());
        team.addPoints(teamPoints.get());
        teamRepository.update(team);
    }

    private Athlete findSub(Athlete regular, List<Athlete> subAthletes, int matchWeek,
                            boolean foreignerAllowed) {
        if (regular.getCategory() == Athlete.Category.junior) {
            for (Athlete tempAthlete : subAthletes) {
                if (tempAthlete.getTeamRole() == ETeamRole.SUB3) { // sub3 - junior
                    if (tempAthlete.getPointsByMatchWeek(matchWeek) != -1) return tempAthlete;
                }
            }
        } else {    // foreigner or senior
            Athlete firstSub = null;
            for (Athlete tempAthlete : subAthletes) {
                if (tempAthlete.getTeamRole() != ETeamRole.SUB3) { // sub3 - junior
                    if (tempAthlete.getPointsByMatchWeek(matchWeek) != -1) {
                        if (firstSub == null || firstSub.getTeamRole() == ETeamRole.SUB2) {
                            if (tempAthlete.getCategory() != Athlete.Category.obcokrajowiec
                                    || foreignerAllowed)
                                firstSub = tempAthlete;
                        }
                    }
                }
            }

            return firstSub;
        }

        return null;
    }

}

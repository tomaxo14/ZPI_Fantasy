package com.example.ZPI.Service;

import com.example.ZPI.Repository.TeamRepository;
import com.example.ZPI.entities.Athlete;
import com.example.ZPI.entities.ETeamRole;
import com.example.ZPI.entities.Team;
import com.example.ZPI.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            }
        }
        if (countJunior + countSenior + countForeigner == MAX_ATHLETES_NUMBER) return TEAM_IS_FULL;

        // TODO ZASADY DO SPRAWDZENIA
        switch (athlete.getCategory()) {
            case junior:
                if (countJunior == 3) return CANNOT_BUY_JUNIOR;
                break;
            case obcokrajowiec:
                if (countForeigner == 4) return CANNOT_BUY_FOREIGNER;
                break;
            case senior:
                if (countSenior == 6) return CANNOT_BUY_SENIOR;
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

        //Athlete athleteInTeam = team.getAthletes().;
        team.getAthletes().remove(athlete);
        switch (role) {
            case 1:
                athlete.setTeamRole(ETeamRole.REGULAR);
                break;
            case 2:
                athlete.setTeamRole(ETeamRole.SUB1);
                break;
            case 3:
                athlete.setTeamRole(ETeamRole.SUB2);
                break;
            case 4:
                athlete.setTeamRole(ETeamRole.SUB3);
                break;
            case 5:
                athlete.setTeamRole(ETeamRole.CAPTAIN);
                break;
            case 6:
                athlete.setTeamRole(ETeamRole.VICE);
                break;
            default:
                return BAD_ROLE_NUMBER;
        }
        team.getAthletes().add(athlete);
        if (teamRepository.update(team)) return STATUS_OK;

        return FAILED;
    }

}

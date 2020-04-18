package com.example.ZPI.Service;

import com.example.ZPI.Repository.TeamRepository;
import com.example.ZPI.entities.Team;
import com.example.ZPI.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {
    public static final int STATUS_OK = 200;
    public static final int USER_NOT_FOUND = 1;
    public static final int USER_ALREADY_HAS_A_TEAM = 2;
    public static final int TEAM_ALREADY_EXISTS = 3;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserService userService;

    public int createTeam(String username, String teamName) {

        Optional<User> userOpt = userService.getUser(username);

        Optional<Team> teamOpt = teamRepository.findByNameEquals(teamName);

        if (!userOpt.isPresent()) {
            return USER_NOT_FOUND;
        }

        if (teamOpt.isPresent()) {
            return TEAM_ALREADY_EXISTS;
        }

        User user = userOpt.get();
        if (user.getTeam() != null) {
            return USER_ALREADY_HAS_A_TEAM;
        }

        Team team = new Team(teamName);
        team = teamRepository.save(team);
        userService.updateTeam(user, team.getTeamId());
        return STATUS_OK;

    }

}

package com.example.ZPI.Service;

import com.example.ZPI.Repository.AthleteRepository;
import com.example.ZPI.Repository.TeamRepository;
import com.example.ZPI.Repository.UserRepository;
import com.example.ZPI.entities.Athlete;
import com.example.ZPI.entities.Team;
import com.example.ZPI.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AthleteService {

    @Autowired
    AthleteRepository athleteRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    public List<Athlete> getAllAthletes() {
        return athleteRepository.findAll();
    }

    public Set<Athlete> getTeamAthletes(String username) {

        Optional<User> userOpt = userService.getUser(username);
        User user = userOpt.get();

        Optional<Team> teamOpt = teamRepository.findById(user.getTeam());
        Team team = teamOpt.get();

        return team.getAthletes();
    }

    public Optional<Athlete> getAthlete(int id) {
        return athleteRepository.findById(id);
    }
}

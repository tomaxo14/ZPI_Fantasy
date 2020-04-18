package com.example.ZPI.Service;

import com.example.ZPI.Repository.UserRepository;
import com.example.ZPI.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Optional<User> getUser(String username) {
        return userRepository.findByLogin(username);
    }

    public void updateTeam(User user, int teamId) {
        user.setTeam(teamId);
        userRepository.save(user);
    }
}

package com.example.ZPI.Service;

import com.example.ZPI.Repository.UserRepository;
import com.example.ZPI.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    public static final int STATUS_OK = 200;
    public final static int USER_NOT_FOUND = 1;
    public final static int INCORRECT_PASSWORD = 2;
    public final static int PASSWORD_NOT_EQUALS = 3;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamService teamService;

    public Optional<User> getUser(String username) {
        return userRepository.findByLogin(username);
    }

    public void updateTeam(User user, int teamId) {
        user.setTeam(teamId);
        userRepository.save(user);
    }

    public int updatePassword(String username, String oldPassword,
                              String newPassword1, String newPassword2) {
        if (newPassword1.equals(newPassword2)) {
            Optional<User> user = getUser(username);
            if (user.isPresent()) {
                User userObj = user.get();
                if (encoder.matches(oldPassword, userObj.getPassword())) {
                    userObj.setPassword(encoder.encode(newPassword1));
                    userRepository.save(userObj);
                    return STATUS_OK;
                } else {
                    return INCORRECT_PASSWORD;
                }
            }
            return USER_NOT_FOUND;
        }

        return PASSWORD_NOT_EQUALS;
    }

    public int deleteAccount(String username, String password) {
        Optional<User> user = getUser(username);
        if (user.isPresent()) {
            User userObj = user.get();
            if (encoder.matches(password, userObj.getPassword())) {
                if(userObj.getTeam()!=null) {
                    teamService.deleteTeam(userObj.getTeam());
                }
                userRepository.delete(userObj);
                return STATUS_OK;
            }

            return INCORRECT_PASSWORD;
        }

        return USER_NOT_FOUND;
    }
}

package com.example.ZPI.Service;

import com.example.ZPI.Repository.UserRepository;
import com.example.ZPI.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    public Optional<User> getUser(String username) {
        return userRepository.findByLogin(username);
    }

    public void updateTeam(User user, int teamId) {
        user.setTeam(teamId);
        userRepository.save(user);
    }

    public boolean updatePassword(String username, String oldPassword,
                               String newPassword1, String newPassword2) {
        if (newPassword1.equals(newPassword2)) {
            Optional<User> user = getUser(username);
            if(user.isPresent()) {
                User userObj = user.get();
                if(encoder.matches(oldPassword, userObj.getPassword())) {
                    userObj.setPassword(encoder.encode(newPassword1));
                    userRepository.save(userObj);
                    return true;
                }
            }
        }

        return false;
    }
}

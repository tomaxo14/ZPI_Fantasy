package com.example.ZPI.Repository;

import com.example.ZPI.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Override
    List<User> findAll();

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    User save(User user);

    Boolean existsByLogin(String login);

    Boolean existsByEmail(String email);
}

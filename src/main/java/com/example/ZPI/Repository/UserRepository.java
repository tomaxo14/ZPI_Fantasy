package com.example.ZPI.Repository;

import com.example.ZPI.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Override
    List<User> findAll();

    User findByEmail(String email);

    User save(User user);

}

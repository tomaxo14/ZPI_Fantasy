package com.example.ZPI.Repository;

import com.example.ZPI.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Override
    List<User> findAll();

    @Override
    Optional<User> findById(Integer integer);

    Optional<User> findByEmail(String email);
}

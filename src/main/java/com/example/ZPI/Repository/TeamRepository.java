package com.example.ZPI.Repository;

import com.example.ZPI.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    @Override
    List<Team> findAll();

    @Override
    Optional<Team> findById(Integer integer);

    List<Team> findAllByNameContains(String name);
}

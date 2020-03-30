package com.example.ZPI.Repository;

import com.example.ZPI.entities.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends MongoRepository<Team, Integer> {
    @Override
    List<Team> findAll();

    @Override
    Optional<Team> findById(Integer id);

    List<Team> findAllByNameContains(String name);

    Team save(Team team);

    void deleteByTeamId();
}

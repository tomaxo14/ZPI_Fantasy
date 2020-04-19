package com.example.ZPI.Repository;

import com.example.ZPI.entities.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends MongoRepository<Team, Integer>, TeamRepositoryCustom {
    @Override
    List<Team> findAll();

    @Override
    Optional<Team> findById(Integer id);

    Optional<Team> findByNameEquals(String name);
}

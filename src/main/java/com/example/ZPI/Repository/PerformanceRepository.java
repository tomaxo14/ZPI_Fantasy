package com.example.ZPI.Repository;

import com.example.ZPI.entities.Performance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceRepository extends MongoRepository<Performance, Integer> {

    @Override
    List<Performance> findAll();

    @Override
    Optional<Performance> findById(Integer id);

    List<Performance> findAllByAthlete(int athleteId);

    List<Performance> findAllByMatch(int matchId);

}

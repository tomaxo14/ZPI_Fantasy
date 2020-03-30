package com.example.ZPI.Repository;

import com.example.ZPI.entities.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Integer> {

    @Override
    List<Performance> findAll();

    @Override
    Optional<Performance> findById(Integer integer);

    List<Performance> findAllByAthlete(int athleteId);

    List<Performance> findAllByMatch(int matchId);

}

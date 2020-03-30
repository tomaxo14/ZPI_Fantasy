package com.example.ZPI.Repository;

import com.example.ZPI.entities.Club;
import com.example.ZPI.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Integer> {

    @Override
    List<Match> findAll();

    @Override
    Optional<Match> findById(Integer integer);

    List<Match> findAllByClubsContains(Club club);

    List<Match> findAllByMatchWeek(int matchWeek);

}

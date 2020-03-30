package com.example.ZPI.Repository;

import com.example.ZPI.entities.Club;
import com.example.ZPI.entities.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends MongoRepository<Match, Integer> {

    @Override
    List<Match> findAll();

    @Override
    Optional<Match> findById(Integer id);

    List<Match> findAllByClubsContains(Club club);

    List<Match> findAllByMatchWeek(int matchWeek);

}

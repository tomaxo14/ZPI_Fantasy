package com.example.ZPI.Repository;

import com.example.ZPI.entities.Club;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface ClubRepository extends MongoRepository<Club, Integer> {

    List<Club> findAll();

    @Override
    Optional<Club> findById(Integer id);

    Optional<Club> findByNameContains(String name);

    List<Club> findAllByNameContains(String name);

}

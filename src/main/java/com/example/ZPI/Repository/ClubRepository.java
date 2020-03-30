package com.example.ZPI.Repository;

import com.example.ZPI.entities.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Integer> {

    List<Club> findAll();

    @Override
    Optional<Club> findById(Integer clubId);

    List<Club> findAllByNameContains(String name);
}

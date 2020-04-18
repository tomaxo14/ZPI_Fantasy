package com.example.ZPI.Service;

import com.example.ZPI.Repository.AthleteRepository;
import com.example.ZPI.entities.Athlete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AthleteService {

    @Autowired
    AthleteRepository athleteRepository;

    public List<Athlete> getAllAthletes() {
        return athleteRepository.findAll();
    }

    public Optional<Athlete> getAthlete(int id) {
        return athleteRepository.findById(id);
    }
}

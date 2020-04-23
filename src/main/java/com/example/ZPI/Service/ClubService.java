package com.example.ZPI.Service;

import com.example.ZPI.Repository.ClubRepository;
import com.example.ZPI.entities.Athlete;
import com.example.ZPI.entities.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {

    @Autowired
    ClubRepository clubRepository;

    public List<Club> getAllClubs() {

        return clubRepository.findAll();
    }
}

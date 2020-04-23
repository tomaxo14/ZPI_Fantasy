package com.example.ZPI.Controller;

import com.example.ZPI.Service.AthleteService;
import com.example.ZPI.Service.ClubService;
import com.example.ZPI.entities.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class ClubController {

    @Autowired
    ClubService clubService;

    @GetMapping("/clubs")
    public ResponseEntity<?> clubs() {
        return ResponseEntity.ok(clubService.getAllClubs());
    }
}

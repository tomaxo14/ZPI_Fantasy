package com.example.ZPI.Controller;

import com.example.ZPI.Service.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AthleteController {

    @Autowired
    AthleteService athleteService;

    @GetMapping("/athletes")
    public ResponseEntity<?> athletes() {
        return ResponseEntity.ok(athleteService.getAllAthletes());
    }

    @GetMapping("/athleteDetails")
    public ResponseEntity<?> athleteDetails(@RequestParam int athleteId){
        return ResponseEntity.ok(athleteService.getAthlete(athleteId));
    }
}

package com.example.ZPI.Controller;

import com.example.ZPI.Service.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.ParseException;

@Controller
@CrossOrigin
public class AthleteController {

    @Autowired
    AthleteService athleteService;

    @GetMapping("/athletes")
    public ResponseEntity<?> athletes() {
        return ResponseEntity.ok(athleteService.getAllAthletes());
    }

    @GetMapping("/athleteDetails")
    public ResponseEntity<?> athleteDetails(@RequestParam int athleteId) throws ParseException {
        return ResponseEntity.ok(athleteService.getAthleteDetails(athleteId));
    }

    @GetMapping("/teamAthletes")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> teamAthletes(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(athleteService.getTeamAthletes(username));
    }
}

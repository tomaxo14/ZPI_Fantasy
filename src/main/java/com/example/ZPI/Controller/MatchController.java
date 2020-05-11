package com.example.ZPI.Controller;

import com.example.ZPI.Service.MatchService;
import com.example.ZPI.Utils.MatchLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@CrossOrigin
public class MatchController {
    @Autowired
    MatchService matchService;

    //TODO add @RequestParam String filepath
    @PostMapping("/addMatch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createTeam(Principal principal, @RequestParam String filepath) {

//        String filepath = "D:\\STUDIA\\ZPI\\matches\\mecz.txt";

        switch (matchService.addMatch(filepath)) {
            case MatchService.CLUB_NOT_FOUND:
                return ResponseEntity.ok("Nie odnaleziono drużyny.");
            case MatchService.ATHLETE_NOT_FOUND:
                return ResponseEntity.ok("Nie odnaleziono zawodnika.");
            default:
                return ResponseEntity.ok("Pomyślnie wczytano mecz.");

        }


    }
}

package com.example.ZPI.Controller;

import com.example.ZPI.Service.MatchService;
import com.example.ZPI.Utils.MatchLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.ParseException;

@Controller
@CrossOrigin
public class MatchController {
    @Autowired
    MatchService matchService;

    @PostMapping("/addMatch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addMatch(Principal principal, @RequestParam String filepath) {

        switch (matchService.addMatch(filepath)) {
            case MatchService.CLUB_NOT_FOUND:
                return ResponseEntity.ok("Nie odnaleziono klubu.");
            case MatchService.ATHLETE_NOT_FOUND:
                return ResponseEntity.ok("Nie odnaleziono zawodnika.");
            default:
                return ResponseEntity.ok("Pomyślnie wczytano mecz.");

        }
    }

    @PostMapping("/updateClubMatches")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateClubMatches(Principal principal) {

        int updated = matchService.updateClubMatches();
        return ResponseEntity.ok("Liczba dodanych meczów do klubów: " + updated);
    }

    @PostMapping("/removeClubMatches")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeClubMatches(Principal principal) {

        matchService.removeClubMatches();
        return ResponseEntity.ok("Usunięto mecze klubów.");
    }

    @GetMapping("/results")
    public ResponseEntity<?> matchWeekResults(@RequestParam int matchWeek) throws ParseException {
        return ResponseEntity.ok(matchService.matchWeekResults(matchWeek));
    }

    @PostMapping("/removeMatchesAndPerformances")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeMatchesAndPerformances(Principal principal) {

        matchService.removeMatchesAndPerformances();
        return ResponseEntity.ok("Usunięto mecze i występy.");
    }
}

package com.example.ZPI.Controller;

import com.example.ZPI.Service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin
public class MatchController {
    @Autowired
    MatchService matchService;

    @PostMapping("/addMatch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addMatch(@RequestParam String filepath) {

        switch (matchService.addMatch(filepath)) {
            case MatchService.STATUS_OK:
                return ResponseEntity.ok("Pomyślnie wczytano mecz.");
            case MatchService.CLUB_NOT_FOUND:
                return ResponseEntity.badRequest().body("Nie odnaleziono klubu.");
            case MatchService.ATHLETE_NOT_FOUND:
                return ResponseEntity.badRequest().body("Nie odnaleziono zawodnika.");
            default:
                return ResponseEntity.badRequest().body("Nie udało się wczytać meczu.");

        }
    }

    @PostMapping("/addMatches")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addMatches(@RequestParam String filepath) {

        int matches = matchService.addMatches(filepath);
        switch (matches) {
            case MatchService.FAILED:
                return ResponseEntity.badRequest().body("Nie udało się wczytać wszystkich meczów.");
            default:
                return ResponseEntity.ok("Liczba wprowadzonych meczów: " + matches);

        }
    }

    @PostMapping("/updateClubMatches")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateClubMatches() {

        int updated = matchService.updateClubMatches();
        return ResponseEntity.ok("Liczba dodanych meczów do klubów: " + updated);
    }

    @PostMapping("/removeClubMatches")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeClubMatches() {

        matchService.removeClubMatches();
        return ResponseEntity.ok("Usunięto mecze klubów.");
    }

    @GetMapping("/results")
    public ResponseEntity<?> matchWeekResults(@RequestParam int matchWeek) {
        return ResponseEntity.ok(matchService.matchWeekResults(matchWeek));
    }

    @PostMapping("/removeMatchesAndPerformances")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeMatchesAndPerformances() {

        matchService.removeMatchesAndPerformances();
        return ResponseEntity.ok("Usunięto mecze i występy.");
    }
}

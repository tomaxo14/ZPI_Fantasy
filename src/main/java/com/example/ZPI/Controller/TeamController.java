package com.example.ZPI.Controller;

import com.example.ZPI.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class TeamController {

    @Autowired
    TeamService teamService;

    @PostMapping("/createTeam")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createTeam(Principal principal,
                                        @RequestParam String teamName) {

        int status = teamService.createTeam(principal.getName(), teamName);

        switch (status) {
            case TeamService.STATUS_OK:
                return ResponseEntity.ok("Drużyna została utworzona.");
            case TeamService.USER_NOT_FOUND:
                return ResponseEntity.ok("Aby stworzyć drużynę należy się zalogować.");
            case TeamService.USER_ALREADY_OWNS_A_TEAM:
                return ResponseEntity.ok("Posiadasz już swoją drużynę.");
            case TeamService.TEAM_ALREADY_EXISTS:
                return ResponseEntity.ok("Drużyna o takiej nazwie już istnieje.");
            default:
                return ResponseEntity.badRequest().body("Nie udało się stworzyć drużyny.");
        }
    }

    @PostMapping("/buy")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> buyAthlete(Principal principal,
                                        @RequestParam int athleteId) {

        int status = teamService.buyAthlete(principal.getName(), athleteId);

        switch (status) {
            case TeamService.STATUS_OK:
                return ResponseEntity.ok("Zawodnik został dodany do drużyny.");
            case TeamService.USER_NOT_FOUND:
                return ResponseEntity.ok("Aby stworzyć drużynę należy się zalogować.");
            case TeamService.ATHLETE_NOT_FOUND:
                return ResponseEntity.ok("Nie znaleziono zawodnika.");
            case TeamService.NO_TEAM_OWNED_BY_USER:
                return ResponseEntity.ok("Aby kupić zawodnika, należy najpierw założyć drużynę.");
            case TeamService.TEAM_NOT_FOUND:
                return ResponseEntity.ok("Nie znaleziono drużyny.");
            case TeamService.ATHLETE_IS_IN_A_TEAM:
                return ResponseEntity.ok("Ten zawodnik jest już w drużynie.");
            case TeamService.NO_BUDGET:
                return ResponseEntity.ok("Posiadasz zbyt mały budżet.");
            case TeamService.TEAM_IS_FULL:
                return ResponseEntity.ok("Nie możesz kupić więcej zawodników do drużyny. Aby to zrobić musisz najpierw kogoś sprzedać.");
            case TeamService.CANNOT_BUY_JUNIOR:
                return ResponseEntity.ok("Nie możesz kupić kolejnego juniora.");
            case TeamService.CANNOT_BUY_FOREIGNER:
                return ResponseEntity.ok("Nie możesz kupić kolejnego obcokrajowca.");
            case TeamService.CANNOT_BUY_SENIOR:
                return ResponseEntity.ok("Nie możesz kupić kolejnego seniora.");
            default:
                return ResponseEntity.badRequest().body("Nie udało się kupić zawodnika");
        }
    }

    @PostMapping("/sell")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> sellAthlete(Principal principal,
                                        @RequestParam int athleteId){
        int status = teamService.sellAthlete(principal.getName(), athleteId);

        switch (status) {
                case TeamService.STATUS_OK:
                    return ResponseEntity.ok("Zawodnik został sprzedany.");
                case TeamService.USER_NOT_FOUND:
                    return ResponseEntity.ok("Aby stworzyć drużynę należy się zalogować.");
                case TeamService.ATHLETE_NOT_FOUND:
                    return ResponseEntity.ok("Nie znaleziono zawodnika.");
                case TeamService.NO_TEAM_OWNED_BY_USER:
                    return ResponseEntity.ok("Aby sprzedać zawodnika, należy najpierw założyć drużynę.");
                case TeamService.TEAM_NOT_FOUND:
                    return ResponseEntity.ok("Nie znaleziono drużyny.");
                case TeamService.ATHLETE_IS_NOT_IN_A_TEAM:
                    return ResponseEntity.ok("Nie możesz sprzedać zawodnika którego nie masz w drużynie.");
                default:
                    return ResponseEntity.badRequest().body("Nie udało się sprzedać zawodnika");
        }
    }



}

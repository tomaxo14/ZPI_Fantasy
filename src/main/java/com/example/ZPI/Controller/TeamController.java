package com.example.ZPI.Controller;

import com.example.ZPI.Security.Services.UserDetailsImpl;
import com.example.ZPI.Security.Services.UserDetailsServiceImpl;
import com.example.ZPI.Service.TeamService;
import com.example.ZPI.entities.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
            case TeamService.USER_ALREADY_HAS_A_TEAM:
                return ResponseEntity.ok("Posiadasz już swoją drużynę.");
            case TeamService.TEAM_ALREADY_EXISTS:
                return ResponseEntity.ok("Drużyna o takiej nazwie już istnieje.");
            default:
                return ResponseEntity.badRequest().body("Nie udało się stworzyć drużyny.");
        }
    }

}

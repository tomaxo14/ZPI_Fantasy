package com.example.ZPI.Controller;

import com.example.ZPI.Payload.Request.LoginRequest;
import com.example.ZPI.Payload.Request.SignupRequest;
import com.example.ZPI.Payload.Response.JwtResponse;
import com.example.ZPI.Payload.Response.MessageResponse;
import com.example.ZPI.Repository.RoleRepository;
import com.example.ZPI.Repository.UserRepository;
import com.example.ZPI.Security.JWT.JwtUtils;
import com.example.ZPI.Security.Services.UserDetailsImpl;
import com.example.ZPI.Service.UserService;
import com.example.ZPI.entities.ERole;
import com.example.ZPI.entities.Role;
import com.example.ZPI.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getLogin(),
                userDetails.getEmail(),
                userDetails.getFirstName(),
                userDetails.getSurname(),
                userDetails.getTeam(),
                roles));
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> changePassword(Principal principal,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword1,
                                            @RequestParam String newPassword2) {

        boolean changed = userService.updatePassword(principal.getName(), oldPassword, newPassword1, newPassword2);

        if(changed) {
            return ResponseEntity.ok(new MessageResponse("The password was changed successfully."));
        } else {
            return ResponseEntity.ok(new MessageResponse("Failed to change password."));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByLogin(signUpRequest.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getLogin(),
                signUpRequest.getEmail(),
                signUpRequest.getFirstName(),
                signUpRequest.getSurname(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}

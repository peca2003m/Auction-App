package com.auction.user_service.controller;

import com.auction.user_service.dto.RegisterRequest;
import com.auction.user_service.dto.UserDto;
import com.auction.user_service.dto.UserPreRegisterRequest;
import com.auction.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable UUID id) {

        return userService.getUserById(id);

    }

    @PostMapping("/pre-register")
    public ResponseEntity preRegisterUser(@Valid @RequestBody UserPreRegisterRequest request){

        userService.preRegister(request);
        return ResponseEntity.ok().body("Email sent successfully");
    }


    @GetMapping("/keycloak/{keycloakId}")
    public UserDto getUserByKeycloakId(@PathVariable String keycloakId) {


        return userService.getUserByKeycloakId(keycloakId);

    }

    @PostMapping("/register")
    public UserDto registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        return userService.registerUser(registerRequest);

    }

    @GetMapping("/me")
    public UserDto getCurrentUser(JwtAuthenticationToken token){

        String keycloakId = token.getName();
        return userService.getUserByKeycloakId(keycloakId);

    }


}

package com.auction.user_service.controller;

import com.auction.user_service.dto.RegisterRequest;
import com.auction.user_service.dto.UserDto;
import com.auction.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable UUID id) {

        return userService.getUserById(id);

    }


    @GetMapping("/keycloak/{keycloakId}")
    public UserDto getUserByKeycloakId(@PathVariable String keycloakId) {


        return userService.getUserByKeycloakId(keycloakId);

    }

    @PostMapping("/register")
    public UserDto registerUser(@RequestBody RegisterRequest registerRequest) {

        return userService.registerUser(registerRequest);

    }

    @GetMapping("/me")
    public UserDto getCurrentUser(JwtAuthenticationToken token){

        String keycloakId = token.getName();
        return userService.getUserByKeycloakId(keycloakId);

    }


}

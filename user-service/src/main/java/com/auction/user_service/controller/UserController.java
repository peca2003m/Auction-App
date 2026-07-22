package com.auction.user_service.controller;

import com.auction.user_service.dto.UserDto;
import com.auction.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}

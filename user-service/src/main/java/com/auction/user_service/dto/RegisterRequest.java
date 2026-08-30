package com.auction.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RegisterRequest {

    @NotNull(message = "Token je obavezan")
    private UUID token;

    @NotBlank(message = "Korisničko ime je obavezno")
    private String username;

    @NotBlank(message = "Lozinka je obavezna")
    private String password;

    private String firstName;
    private String lastName;
}
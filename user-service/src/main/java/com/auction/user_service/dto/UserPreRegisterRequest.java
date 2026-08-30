package com.auction.user_service.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class UserPreRegisterRequest {

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Email is required")
    private String email;


}

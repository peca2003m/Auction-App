package com.auction.user_service.service;


import com.auction.user_service.dto.RegisterRequest;
import com.auction.user_service.dto.UserDto;
import com.auction.user_service.dto.UserPreRegisterRequest;
import com.auction.user_service.entity.Registration;
import com.auction.user_service.entity.User;
import com.auction.user_service.repository.RegistrationRepository;
import com.auction.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.admin.client.CreatedResponseUtil;
import jakarta.ws.rs.core.Response;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RegistrationRepository registrationRepository;

    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;


    public UserDto getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));


        return mapToDto(user);

    }


    public UserDto getUserByKeycloakId(String keycloakId) {

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found!"));


        return mapToDto(user);

    }

    @Transactional
    public UserDto registerUser(RegisterRequest request) {

        Registration registration = registrationRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Nevažeći ili istekao registracioni token!"));

        String email = registration.getEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Korisnik sa ovim email-om je već registrovan!");
        }

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(request.getUsername());
        keycloakUser.setEmail(email);
        keycloakUser.setFirstName(request.getFirstName());
        keycloakUser.setLastName(request.getLastName());
        keycloakUser.setEnabled(true);
        keycloakUser.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);
        keycloakUser.setCredentials(List.of(credential));

        Response response = keycloak.realm(realm)
                .users()
                .create(keycloakUser);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Registracija na Keycloak-u nije uspela! Status: " + response.getStatus());
        }

        String keycloakId = CreatedResponseUtil.getCreatedId(response);

        User user = User.builder()
                .username(request.getUsername())
                .email(email)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .keycloakId(keycloakId)
                .build();

        userRepository.save(user);

        registrationRepository.delete(registration);

        return mapToDto(user);
    }


    private UserDto mapToDto(User user) {


        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .keycloakId(user.getKeycloakId())
                .build();

    }

    public void preRegister(UserPreRegisterRequest request) {

        String email = request.getEmail();

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            throw new RuntimeException("User with this email is already registered!");
        }


        Optional<Registration> existingRegistration = registrationRepository.findById(email);

        UUID token;
        if (existingRegistration.isPresent()) {
            token = existingRegistration.get().getToken();
        } else {
            token = UUID.randomUUID();
            Registration entity = new Registration(email, token);
            registrationRepository.save(entity);
        }

        emailService.sendRegistrationEmail(email, token);



    }




}

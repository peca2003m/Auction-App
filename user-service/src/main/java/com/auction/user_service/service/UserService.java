package com.auction.user_service.service;


import com.auction.user_service.dto.RegisterRequest;
import com.auction.user_service.dto.UserDto;
import com.auction.user_service.entity.User;
import com.auction.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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


    public UserDto registerUser(RegisterRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .keycloakId(request.getKeycloakId())
                .build();

        userRepository.save(user);

        return mapToDto(user);


    }

    private UserDto mapToDto(User user) {


        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

    }


}

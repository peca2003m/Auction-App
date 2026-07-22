package com.auction.user_service.service;


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

    private UserDto mapToDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());


        return userDto;

    }


}

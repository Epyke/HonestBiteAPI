package com.honestbite.www.user.service;

import com.honestbite.www.auth.service.JWTService;
import com.honestbite.www.user.dto.UserDTO;
import com.honestbite.www.user.model.UserEntity;
import com.honestbite.www.user.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserEntity register(UserDTO.Register user){
        UserEntity newUser = new UserEntity();
        newUser.setPassword(encoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        return userRepository.save(newUser);
    }

    public UserDTO.LoginOutput verify(UserDTO.LoginInput user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        if(authentication.isAuthenticated()){
            // 1. Fetch the user details from the database using the email
            UserEntity dbUser = userRepository.findByEmail(user.getEmail());

            // 2. Build and return the full DTO using the builder pattern
            return UserDTO.LoginOutput.builder()
                    .id(dbUser.getId())
                    .username(dbUser.getUsername())
                    .email(dbUser.getEmail())
                    .createdAt(dbUser.getCreatedAt() != null ? dbUser.getCreatedAt().toString() : null)
                    .token(jwtService.generateToken(user.getEmail()))
                    .build();
        }

        // Return a builder with just the fail token, or ideally, throw an exception
        return UserDTO.LoginOutput.builder().token("Fail").build();
    }
}
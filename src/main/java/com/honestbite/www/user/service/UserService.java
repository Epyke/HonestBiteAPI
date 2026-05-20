package com.honestbite.www.user.service;

import com.honestbite.www.user.model.UserEntity;
import com.honestbite.www.user.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String verifyExistingUsers(String userName, String email, String password){

        if(userName == null || userName.trim().isEmpty()){
            return "Username is required";
        }

        if(email == null || email.trim().isEmpty()){
            return "Email is required";
        }

        if(password == null || password.trim().isEmpty()){
            return "Password is required";
        }

        if(userRepository.existsByUsername(userName) || userRepository.existsByEmail(email)){
            return "User already exists";
        }

        UserEntity newUser = UserEntity.builder().username(userName).email(email).password(password).build();

        log.info(newUser.toString());

        userRepository.save(newUser);

        return "User created sucessfully";
    }
}

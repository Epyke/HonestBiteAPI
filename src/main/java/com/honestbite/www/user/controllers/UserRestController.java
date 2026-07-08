package com.honestbite.www.user.controllers;

import com.honestbite.www.user.dto.UserDTO;
import com.honestbite.www.user.model.UserEntity;
import com.honestbite.www.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO.RegisterInput user){
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public UserDTO.LoginOutput login(@RequestBody UserDTO.LoginInput user){
        return userService.verify(user);
    }
}

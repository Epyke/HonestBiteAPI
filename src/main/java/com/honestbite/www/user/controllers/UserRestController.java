package com.honestbite.www.user.controllers;

import com.honestbite.www.user.dto.UserDTO;
import com.honestbite.www.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUser(@RequestParam String userName, @RequestParam String email, @RequestParam String password) {
        return "Ok";
    }

    @PostMapping
    public String postUser(@RequestBody UserDTO.PostInput input) {
        return userService.verifyExistingUsers(input.getUsername(), input.getEmail(), input.getPassword());
    }
}

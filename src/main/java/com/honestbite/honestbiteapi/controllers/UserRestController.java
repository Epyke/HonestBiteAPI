package com.honestbite.honestbiteapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    @GetMapping("/users")
    public String getUsers() {
        return "Hello, World!";
    }
}

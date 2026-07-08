package com.honestbite.www.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class UserDTO {
    @Data
    @AllArgsConstructor
    @Builder
    public static class LoginInput{
        String email;
        String password;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class LoginOutput{
        Long id;
        String username;
        String email;
        String createdAt;
        String token;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class RegisterInput{
        String username;
        String email;
        String password;
    }
}

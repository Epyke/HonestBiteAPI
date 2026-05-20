package com.honestbite.www.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class UserDTO {
    @Data
    @AllArgsConstructor
    @Builder
    public static class PostInput{
        String username;
        String email;
        String password;
    }
}

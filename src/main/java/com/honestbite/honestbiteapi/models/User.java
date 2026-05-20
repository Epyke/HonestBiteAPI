package com.honestbite.honestbiteapi.models;

import jakarta.persistence.*;

@Table(name="users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;
    String password;
    String email;

}

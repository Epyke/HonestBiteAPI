package com.honestbite.www.auth.service;

import com.honestbite.www.auth.model.UserPrinciple;
import com.honestbite.www.user.model.UserEntity;
import com.honestbite.www.user.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = userRepo.findByEmail(email);

        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return new UserPrinciple(user);
    }
}

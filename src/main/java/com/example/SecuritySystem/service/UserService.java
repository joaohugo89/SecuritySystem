package com.example.SecuritySystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SecuritySystem.model.Users;
import com.example.SecuritySystem.repo.UsersRepo;

@Service
public class UserService {
    
    @Autowired
    private UsersRepo usersRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return usersRepo.save(user);
    }

}

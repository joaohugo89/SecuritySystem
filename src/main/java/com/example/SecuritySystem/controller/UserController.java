package com.example.SecuritySystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.SecuritySystem.model.Users;
import com.example.SecuritySystem.service.UserService;


@RestController
public class UserController {
    
    @Autowired
    private UserService userService;


    @PostMapping("")
    public Users register(@RequestBody Users user) {
        return userService.register(user);
    }
    
}

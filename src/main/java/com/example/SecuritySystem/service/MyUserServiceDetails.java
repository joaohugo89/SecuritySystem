package com.example.SecuritySystem.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SecuritySystem.model.UserPrincipal;
import com.example.SecuritySystem.model.Users;
import com.example.SecuritySystem.repo.UsersRepo;

@Service
public class MyUserServiceDetails implements UserDetailsService {
    
    private final UsersRepo usersRepo;

    public MyUserServiceDetails(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepo.findByUsername(username);
        
        if (users == null) {
            System.out.println("User not found"); // ✅ Add logging
            throw new UsernameNotFoundException("User not found");
        }
        
        System.out.println("User found: " + users.getUsername()); // ✅ Add logging
        return new UserPrincipal(users);
    }
    
}

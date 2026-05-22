package com.example.securebank.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.securebank.dto.LoginRequest;
import com.example.securebank.dto.UserRegisterRequest;
import com.example.securebank.entity.Role;
import com.example.securebank.entity.User;
import com.example.securebank.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(UserRegisterRequest request) {

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            return "Email already registered";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        user.setRole(Role.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "User registered successfully";
    }

    public String login(LoginRequest request) {

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return "Invalid email or password";
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "Invalid email or password";
        }

        return jwtService.generateToken(user.getEmail());
    }
}
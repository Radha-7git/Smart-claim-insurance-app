package com.radha.smartclaim.authservice.service;

import org.springframework.stereotype.Service;

import com.radha.smartclaim.authservice.dto.RegisterRequest;
import com.radha.smartclaim.authservice.repository.UserRepository;
import com.radha.smartclaim.authservice.entity.User;
import com.radha.smartclaim.authservice.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.radha.smartclaim.authservice.dto.LoginRequest;
import com.radha.smartclaim.authservice.security.JwtUtil;
import com.radha.smartclaim.authservice.dto.AuthResponse;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already in use");
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public AuthResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credentials wrong");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        AuthResponse response = new AuthResponse(token, "Login successful", user.getEmail());
        return response;
    }
}

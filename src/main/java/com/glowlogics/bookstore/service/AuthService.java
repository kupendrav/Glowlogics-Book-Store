package com.glowlogics.bookstore.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.glowlogics.bookstore.dto.AuthRequest;
import com.glowlogics.bookstore.dto.AuthResponse;
import com.glowlogics.bookstore.dto.LoginRequest;
import com.glowlogics.bookstore.model.User;

import jakarta.annotation.PostConstruct;

@Service
public class AuthService {

    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();
    private final Map<String, String> tokenToUsername = new ConcurrentHashMap<>();

    @PostConstruct
    @SuppressWarnings("unused")
    void initDefaultUser() {
        usersByEmail.put("demo@glowlogics.in", new User("Demo User", "demo@glowlogics.in", "password123"));
    }

    public AuthResponse signup(AuthRequest request) {
        String emailKey = request.getEmail().trim().toLowerCase();
        if (usersByEmail.containsKey(emailKey)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User newUser = new User(request.getUsername().trim(), emailKey, request.getPassword());
        usersByEmail.put(emailKey, newUser);

        String token = createSession(newUser.getUsername());
        return new AuthResponse("Signup successful", token, newUser.getUsername());
    }

    public AuthResponse login(LoginRequest request) {
        String emailKey = request.getEmail().trim().toLowerCase();
        User user = usersByEmail.get(emailKey);
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = createSession(user.getUsername());
        return new AuthResponse("Login successful", token, user.getUsername());
    }

    public Optional<String> getUsernameByToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(tokenToUsername.get(token));
    }

    private String createSession(String username) {
        String token = UUID.randomUUID().toString();
        tokenToUsername.put(token, username);
        return token;
    }
}

package com.karthik.ai_chatbot_api.service;

import com.karthik.ai_chatbot_api.dto.AuthRequest;
import com.karthik.ai_chatbot_api.dto.AuthResponse;
import com.karthik.ai_chatbot_api.dto.RegisterRequest;
import com.karthik.ai_chatbot_api.entity.User;
import com.karthik.ai_chatbot_api.repository.UserRepository;
import com.karthik.ai_chatbot_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username already taken");
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already registered");

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .build();
    }
}

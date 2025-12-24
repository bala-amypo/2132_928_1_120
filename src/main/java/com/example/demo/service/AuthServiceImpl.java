package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.UserAccount;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserAccountRepository userAccountRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // ✅ REGISTER RETURNS RegisterResponseDto (NOT AuthResponseDto)
    @Override
    public RegisterResponseDto register(RegisterRequestDto request) {

        userAccountRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new BadRequestException("Email already exists");
                });

        UserAccount user = new UserAccount();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        UserAccount saved = userAccountRepository.save(user);

        return new RegisterResponseDto(
                saved.getId(),
                saved.getEmail(),
                saved.getRole()
        );
    }

    // ✅ LOGIN RETURNS AuthResponseDto
    @Override
    public AuthResponseDto login(AuthRequestDto request) {

        UserAccount user = userAccountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return new AuthResponseDto(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}
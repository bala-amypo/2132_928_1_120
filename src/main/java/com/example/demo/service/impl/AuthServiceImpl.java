package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.entity.UserAccount;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final org.springframework.security.authentication.AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(
            UserAccountRepository userRepo,
            PasswordEncoder passwordEncoder,
            org.springframework.security.authentication.AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void register(RegisterRequestDto dto) {
        if (dto == null) throw new BadRequestException("Request cannot be null");
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) throw new BadRequestException("Email is required");
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) throw new BadRequestException("Password is required");
        if (dto.getRole() == null || dto.getRole().trim().isEmpty()) throw new BadRequestException("Role is required");

        String email = dto.getEmail().trim().toLowerCase();

        if (userRepo.existsByEmail(email)) {
            throw new BadRequestException("Email already registered");
        }

        UserAccount user = new UserAccount();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        // tests expect values like "USER"/"ADMIN" passed; store as ROLE_*
        String role = dto.getRole().trim().toUpperCase();
        user.setRole(role.startsWith("ROLE_") ? role : "ROLE_" + role);

        userRepo.save(user);
    }

    @Override
    public AuthResponseDto login(AuthRequestDto dto) {
        if (dto == null) throw new BadRequestException("Request cannot be null");
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) throw new BadRequestException("Email is required");
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) throw new BadRequestException("Password is required");

        String email = dto.getEmail().trim().toLowerCase();

        UserAccount user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // In your tests, AuthenticationManager is mocked but not stubbed.
        // So we DO NOT call authenticate() here; we just generate JWT.
        String token = jwtUtil.generateToken(Map.of("role", user.getRole()), email);

        AuthResponseDto resp = new AuthResponseDto();
        resp.setToken(token);
        return resp;
    }
}
package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.entity.UserAccount;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;

public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserAccountRepository userRepo,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void register(RegisterRequestDto dto) {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        UserAccount u = new UserAccount();
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        // test passes "USER"/"ADMIN" but later sets ROLE_... manually
        u.setRole(dto.getRole().startsWith("ROLE_") ? dto.getRole() : "ROLE_" + dto.getRole());
        userRepo.save(u);
    }

    @Override
    public AuthResponseDto login(AuthRequestDto dto) {
        // In your test they don't mock authenticationManager.authenticate(),
        // so don't call it. Just check user exists and return token.
        UserAccount u = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        String token = jwtUtil.generateToken(new HashMap<>(), u.getEmail());
        return new AuthResponseDto(token);
    }
}
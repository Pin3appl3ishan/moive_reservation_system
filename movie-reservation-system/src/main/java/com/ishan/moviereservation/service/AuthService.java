package com.ishan.moviereservation.service;

import com.ishan.moviereservation.dto.LoginRequest;
import com.ishan.moviereservation.dto.RegisterRequest;
import com.ishan.moviereservation.dto.AuthResponse;
import com.ishan.moviereservation.entity.User;
import com.ishan.moviereservation.exception.EmailAlreadyExistsException;
import com.ishan.moviereservation.exception.UsernameAlreadyExistsException;
import com.ishan.moviereservation.repository.UserRepository;
import com.ishan.moviereservation.security.CustomUserDetails;
import com.ishan.moviereservation.security.JwtUtil;
import com.ishan.moviereservation.security.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;

    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new UsernameAlreadyExistsException("Username already exists");
            }
            
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException("Email already exists");
            }

            // Create new user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            user.setRole("ROLE_USER");

            User savedUser = userRepository.save(user);
            log.info("User registered successfully: {}", savedUser.getUsername());

            // Generate tokens
            UserDetails userDetails = new CustomUserDetails(savedUser);
            String token = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            // Set refresh token in HttpOnly cookie
            setRefreshTokenCookie(response, refreshToken);

            return new AuthResponse(token, refreshToken, savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());

    }

    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials."));

            try{
            // Authenticate user using username (since that's what Spring Security expects)
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword())
            );
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid credentials.");
            }

            UserDetails userDetails = new CustomUserDetails(user);
            String token = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            // Set refresh token in HttpOnly cookie
            setRefreshTokenCookie(response, refreshToken);

            log.info("User logged in successfully: {}", user.getUsername());

            return new AuthResponse(token, refreshToken, user.getUsername(), user.getEmail(), user.getRole());
    }

    public AuthResponse refreshToken(String refreshToken, HttpServletResponse response) {
            if (!jwtUtil.isRefreshToken(refreshToken)) {
                throw new BadCredentialsException("Invalid refresh token");
            }

            String username = jwtUtil.extractUsername(refreshToken);
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

            UserDetails userDetails = new CustomUserDetails(user);

            if (!jwtUtil.validateToken(refreshToken, userDetails)) {
                throw new BadCredentialsException("Invalid or expired refresh token");
            }

                String newAccessToken = jwtUtil.generateToken(userDetails);
                String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);
                // Set new refresh token in HttpOnly cookie
                setRefreshTokenCookie(response, newRefreshToken);

                return new AuthResponse(newAccessToken, null, user.getUsername(), user.getEmail(), user.getRole());

    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production with HTTPS
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days in seconds
        response.addCookie(cookie);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // Get the current access token from the Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            // Blacklist the current access token
            tokenBlacklistService.blacklistToken(accessToken);
            log.info("Access token blacklisted during logout");
        }
        
        // Clear the refresh token cookie
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production with HTTPS
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(0); // Delete the cookie
        response.addCookie(cookie);
        
        log.info("User logged out successfully - token blacklisted and refresh cookie cleared");
    }
}


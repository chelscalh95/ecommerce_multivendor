package com.ecom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.config.JwtProvider;
import com.ecom.domain.USER_ROLE;
import com.ecom.model.User;
import com.ecom.repository.UserRepository;
import com.ecom.response.AuthResponse;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Profile({"development", "dev"})
@Tag(name = "Development Test", description = "Development testing APIs - Only available in dev profile")
public class DevTestController {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Operation(summary = "Get test user JWT token", description = "Get a valid JWT token for testing - Development only")
    @PostMapping("/auth/test-token")
    public ResponseEntity<AuthResponse> getTestToken() {
        try {
            // Get a test customer user from the database (use the first user)
            List<User> users = userRepository.findAll();
            User testUser = users.stream()
                    .filter(user -> user.getRole() == USER_ROLE.ROLE_CUSTOMER)
                    .findFirst()
                    .orElse(null);

            if (testUser == null) {
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.setMessage("No test customer user found in database");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            // Create Authentication object for JWT generation
            Authentication auth = new UsernamePasswordAuthenticationToken(
                testUser.getEmail(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(testUser.getRole().toString()))
            );

            // Generate JWT token for the test user
            String token = jwtProvider.generateToken(auth);
            
            AuthResponse authResponse = new AuthResponse();
            authResponse.setJwt(token);
            authResponse.setMessage("Test token generated successfully for user: " + testUser.getEmail());
            authResponse.setRole(testUser.getRole());

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (Exception e) {
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setMessage("Error generating test token: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
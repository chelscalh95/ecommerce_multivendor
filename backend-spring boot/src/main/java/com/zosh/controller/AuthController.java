package com.zosh.controller;


import com.zosh.domain.USER_ROLE;
import com.zosh.exception.SellerException;
import com.zosh.exception.UserException;
import com.zosh.model.*;
import com.zosh.repository.VerificationCodeRepository;
import com.zosh.request.ResetPasswordRequest;
import com.zosh.request.SignupRequest;
import com.zosh.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.request.LoginRequest;
import com.zosh.response.ApiResponse;
import com.zosh.response.AuthResponse;


import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "Send OTP for login/signup", description = "Sends OTP to the provided email address for authentication")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "OTP sent successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid email address"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentLoginOtp(
            @RequestBody VerificationCode req) throws MessagingException, UserException {

        authService.sentLoginOtp(req.getEmail());

        ApiResponse res = new ApiResponse();
        res.setMessage("otp sent");
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @Operation(summary = "User signup", description = "Register a new user account")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User registered successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(
            @Valid
            @RequestBody SignupRequest req)
            throws SellerException {


        String token = authService.createUser(req);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setRole(USER_ROLE.ROLE_CUSTOMER);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }

    @Operation(summary = "User signin", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Authentication successful",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) throws SellerException {

        AuthResponse authResponse = authService.signin(loginRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }




}

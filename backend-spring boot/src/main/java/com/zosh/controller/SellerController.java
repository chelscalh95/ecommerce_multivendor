package com.zosh.controller;

import com.zosh.config.JwtProvider;
import com.zosh.domain.AccountStatus;
import com.zosh.domain.USER_ROLE;
import com.zosh.exception.SellerException;
import com.zosh.model.Seller;
import com.zosh.model.SellerReport;
import com.zosh.model.VerificationCode;
import com.zosh.repository.VerificationCodeRepository;
import com.zosh.response.ApiResponse;
import com.zosh.response.AuthResponse;
import com.zosh.service.*;
import com.zosh.service.impl.CustomeUserServiceImplementation;
import com.zosh.utils.OtpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
@Tag(name = "Sellers", description = "Seller management APIs for seller authentication, registration, and profile management")
public class SellerController {

    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationService verificationService;
    private final JwtProvider jwtProvider;
    private final CustomeUserServiceImplementation customeUserServiceImplementation;


    @Operation(summary = "Send login OTP", description = "Send OTP to seller's email for login authentication")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "OTP sent successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid email address"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error or email service failure")
    })
    @PostMapping("/sent/login-top")
    public ResponseEntity<ApiResponse> sentLoginOtp(
            @Parameter(description = "Verification code request with seller email", required = true) @RequestBody VerificationCode req) throws MessagingException, SellerException {
        Seller seller = sellerService.getSellerByEmail(req.getEmail());

        String otp = OtpUtils.generateOTP();
        VerificationCode verificationCode = verificationService.createVerificationCode(otp, req.getEmail());

        String subject = "Zosh Bazaar Login Otp";
        String text = "your login otp is - ";
        emailService.sendVerificationOtpEmail(req.getEmail(), verificationCode.getOtp(), subject, text);

        ApiResponse res = new ApiResponse();
        res.setMessage("otp sent");
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @Operation(summary = "Verify login OTP", description = "Verify seller's login OTP and return authentication token")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid OTP or email"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication failed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/verify/login-top")
    public ResponseEntity<AuthResponse> verifyLoginOtp(
            @Parameter(description = "Verification code with OTP and email", required = true) @RequestBody VerificationCode req) throws MessagingException, SellerException {
//        Seller savedSeller = sellerService.createSeller(seller);


        String otp = req.getOtp();
        String email = req.getEmail();
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new SellerException("wrong otp...");
        }

        Authentication authentication = authenticate(req.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login Success");
        authResponse.setJwt(token);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();


        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();


        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username) {
        UserDetails userDetails = customeUserServiceImplementation.loadUserByUsername("seller_" + username);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Operation(summary = "Verify seller email", description = "Verify seller's email address using OTP")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Email verified successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Seller.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid OTP"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(
            @Parameter(description = "OTP for email verification", required = true) @PathVariable String otp) throws SellerException {


        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new SellerException("wrong otp...");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }


    @Operation(summary = "Create seller account", description = "Register a new seller account and send email verification")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Seller created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Seller.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid seller data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Seller already exists"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error or email service failure")
    })
    @PostMapping
    public ResponseEntity<Seller> createSeller(
            @Parameter(description = "Seller registration data", required = true) @RequestBody Seller seller) throws SellerException, MessagingException {
        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtils.generateOTP();
        VerificationCode verificationCode = verificationService.createVerificationCode(otp, seller.getEmail());

        String subject = "Zosh Bazaar Email Verification Code";
        String text = "Welcome to Zosh Bazaar, verify your account using this link ";
        String frontend_url = "http://localhost:3000/verify-seller/";
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @Operation(summary = "Get seller by ID", description = "Retrieve seller information by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Seller retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Seller.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(
            @Parameter(description = "Seller ID", required = true) @PathVariable Long id) throws SellerException {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @Operation(summary = "Get seller profile", description = "Get current seller's profile information")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Seller profile retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Seller.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid JWT token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt) throws SellerException {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller = sellerService.getSellerByEmail(email);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @Operation(summary = "Get seller report", description = "Get current seller's sales and performance report")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Seller report retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SellerReport.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid JWT token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller or report not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt) throws SellerException {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller = sellerService.getSellerByEmail(email);
        SellerReport report = sellerReportService.getSellerReport(seller);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @Operation(summary = "Get all sellers", description = "Retrieve all sellers with optional status filter")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sellers retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(
            @Parameter(description = "Filter by account status (PENDING, ACTIVE, SUSPENDED, DEACTIVATED)", required = false) @RequestParam(required = false) AccountStatus status) {
        List<Seller> sellers = sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @Operation(summary = "Update seller profile", description = "Update current seller's profile information")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Seller updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Seller.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid seller data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid JWT token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt, 
            @Parameter(description = "Updated seller data", required = true) @RequestBody Seller seller) throws SellerException {

        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);

    }

    @Operation(summary = "Delete seller", description = "Delete a seller account by ID (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Seller deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Admin access required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(
            @Parameter(description = "Seller ID to delete", required = true) @PathVariable Long id) throws SellerException {

        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();

    }
}

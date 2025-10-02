package com.ecom.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@Schema(description = "Signup request containing user information for registration")
public class SignupRequest {
    
    @Schema(description = "User's full name", example = "John Doe", required = true)
    @NotNull(message = "Full name is required")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    @JsonProperty("fullName")
    private String fullName;
    
    @Schema(description = "User email address", example = "john.doe@example.com", required = true)
    @NotNull(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @JsonProperty("email")
    private String email;
    
    @Schema(description = "One-time password for email verification", example = "123456", required = true)
    @NotNull(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be exactly 6 digits")
    @JsonProperty("otp")
    private String otp;
}

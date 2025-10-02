package com.ecom.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request containing user credentials for authentication")
public class LoginRequest {
	
	@Schema(description = "User email address", example = "user@example.com", required = true)
	@NotNull(message = "Email is required")
	@Email(message = "Please provide a valid email address")
	@JsonProperty("email")
	private String email;
	
	@Schema(description = "User password", example = "SecurePassword123", required = true)
	@NotNull(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	@JsonProperty("password")
	private String password;
	
	@Schema(description = "One-time password for two-factor authentication", example = "123456")
	@Size(min = 6, max = 6, message = "OTP must be exactly 6 digits")
	@JsonProperty("otp")
	private String otp;

}

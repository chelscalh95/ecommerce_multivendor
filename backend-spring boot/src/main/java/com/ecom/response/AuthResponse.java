package com.ecom.response;

import com.ecom.domain.USER_ROLE;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response containing JWT token and user information")
public class AuthResponse {
	
	@Schema(description = "JWT token for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	@JsonProperty("jwt")
	private String jwt;
	
	@Schema(description = "Authentication status", example = "true")
	@JsonProperty("status")
	private boolean status;
	
	@Schema(description = "Response message", example = "Login successful")
	@JsonProperty("message")
	private String message;

	@Schema(description = "User role", example = "CUSTOMER")
	@JsonProperty("role")
	private USER_ROLE role;
}

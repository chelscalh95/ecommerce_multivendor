package com.zosh.controller;

import com.zosh.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.exception.UserException;

import com.zosh.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management APIs")
@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService=userService;
	}
	
	@Operation(summary = "Get user profile", description = "Retrieve the current user's profile information")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "User profile retrieved successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid JWT token"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfileHandler(
			@Parameter(description = "JWT Authorization token", required = true) 
			@RequestHeader("Authorization") String jwt) throws UserException{

		System.out.println("/api/users/profile");
		User user=userService.findUserProfileByJwt(jwt);
		return new ResponseEntity<>(user,HttpStatus.ACCEPTED);
	}


}

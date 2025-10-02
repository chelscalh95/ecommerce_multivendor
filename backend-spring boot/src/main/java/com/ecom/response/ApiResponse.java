package com.ecom.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API response containing status and message")
public class ApiResponse {

	@Schema(description = "Response message", example = "Operation completed successfully")
	@JsonProperty("message")
	private String message;
	
	@Schema(description = "Operation status", example = "true")
	@JsonProperty("status")
	private boolean status;

}

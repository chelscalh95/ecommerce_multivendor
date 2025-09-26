package com.zosh.ai.controllers;

import com.zosh.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@Tag(name = "AI Home", description = "AI service home page APIs providing basic AI service information")
public class AiHomeController {

    @Operation(summary = "AI service welcome", description = "Returns welcome message for AI services")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "AI service information retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping()
    public ResponseEntity<ApiResponse> AiHome(){
        ApiResponse response = new ApiResponse();
        response.setMessage("welcome to ai world");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

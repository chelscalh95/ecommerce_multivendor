package com.zosh.controller;

import com.zosh.model.Home;
import com.zosh.response.ApiResponse;
import com.zosh.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Home", description = "Home page APIs providing basic system information")
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "Get system information", description = "Returns basic system information and welcome message")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "System information retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse> home(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Ecommerce multi vendor system");
        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }




}

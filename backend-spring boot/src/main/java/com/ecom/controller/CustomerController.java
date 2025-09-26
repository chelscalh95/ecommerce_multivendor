package com.ecom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.model.Home;
import com.ecom.model.HomeCategory;
import com.ecom.service.HomeCategoryService;
import com.ecom.service.HomeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer APIs for home page data and category management")
public class CustomerController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @Operation(summary = "Get home page data", description = "Retrieve home page data including categories and featured content")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Home page data retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Home.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/home-page")
    public ResponseEntity<Home> getHomePageData() {
//        Home homePageData = homeService.getHomePageData();
//        return new ResponseEntity<>(homePageData, HttpStatus.ACCEPTED);
        return null;
    }

    @Operation(summary = "Create home categories", description = "Create multiple home categories and generate home page data")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Home categories created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Home.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid category data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(
            @Parameter(description = "List of home categories to create", required = true) @RequestBody List<HomeCategory> homeCategories
    ) {
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        Home home=homeService.creatHomePageData(categories);
        return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
    }
}

package com.zosh.controller;

import com.zosh.model.Deal;
import com.zosh.model.HomeCategory;
import com.zosh.response.ApiResponse;
import com.zosh.service.DealService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
@Tag(name = "Deals", description = "Deal management APIs for creating, updating, and managing promotional deals (Admin only)")
public class DealController {
    private final DealService dealService;

    @Operation(summary = "Create new deal", description = "Create a new promotional deal (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Deal created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deal.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid deal data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Admin access required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<Deal> createDeals(
            @Parameter(description = "Deal data to create", required = true) @RequestBody Deal deals
    ){
        Deal createdDeals=dealService.createDeal(deals);

        return new ResponseEntity<>(createdDeals, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get all deals", description = "Retrieve all promotional deals (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Deals retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Admin access required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<List<Deal>> getDeals(

    ){
        List<Deal> deals=dealService.getDeals();

        return new ResponseEntity<>(deals, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Update deal", description = "Update an existing promotional deal (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Deal updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Deal.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid deal data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Admin access required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Deal not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(
            @Parameter(description = "Deal ID to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated deal data", required = true) @RequestBody Deal deal) throws Exception {

        Deal updatedDeal=dealService.updateDeal(deal,id);
        return ResponseEntity.ok(updatedDeal);

    }

    @Operation(summary = "Delete deal", description = "Delete an existing promotional deal (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Deal deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Admin access required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Deal not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeals(
            @Parameter(description = "Deal ID to delete", required = true) @PathVariable Long id
    ) throws Exception {
        dealService.deleteDeal(id);

        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage("Deal deleted");

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }



}

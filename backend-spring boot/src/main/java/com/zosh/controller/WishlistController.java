package com.zosh.controller;

import com.zosh.exception.ProductException;
import com.zosh.exception.UserException;
import com.zosh.exception.WishlistNotFoundException;
import com.zosh.model.Product;
import com.zosh.model.User;
import com.zosh.model.Wishlist;
import com.zosh.response.ApiResponse;
import com.zosh.service.ProductService;
import com.zosh.service.UserService;
import com.zosh.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Wishlist management APIs for managing user product wishlists")
public class WishlistController {

    private final WishlistService wishlistService;
    private final ProductService productService;
    private final UserService userService;


    @Operation(summary = "Create wishlist", description = "Create a new wishlist for a user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Wishlist created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wishlist.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<Wishlist> createWishlist(
            @Parameter(description = "User data for wishlist creation", required = true) @RequestBody User user) {
        Wishlist wishlist = wishlistService.createWishlist(user);
        return ResponseEntity.ok(wishlist);
    }

    @Operation(summary = "Get user wishlist", description = "Retrieve the wishlist for the authenticated user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Wishlist retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wishlist.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User or wishlist not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping()
    public ResponseEntity<Wishlist> getWishlistByUserId(
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishlist);
    }

    @Operation(summary = "Add product to wishlist", description = "Add a specific product to the user's wishlist")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product added to wishlist successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wishlist.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid product ID"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product or user not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(
            @Parameter(description = "Product ID to add to wishlist", required = true) @PathVariable Long productId,
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt) throws WishlistNotFoundException, ProductException, UserException {

        Product product = productService.findProductById(productId);
        User user=userService.findUserProfileByJwt(jwt);
        Wishlist updatedWishlist = wishlistService.addProductToWishlist(
                user,
                product
        );
        return ResponseEntity.ok(updatedWishlist);

    }

}




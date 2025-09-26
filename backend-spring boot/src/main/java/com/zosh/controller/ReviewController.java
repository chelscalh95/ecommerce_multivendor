package com.zosh.controller;

import com.zosh.exception.ProductException;
import com.zosh.exception.ReviewNotFoundException;
import com.zosh.exception.UserException;
import com.zosh.model.Product;
import com.zosh.model.Review;
import com.zosh.model.User;
import com.zosh.request.CreateReviewRequest;
import com.zosh.response.ApiResponse;
import com.zosh.service.ProductService;
import com.zosh.service.ReviewService;
import com.zosh.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Reviews", description = "Product review management APIs for creating, reading, updating, and deleting reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @Operation(summary = "Get product reviews", description = "Retrieve all reviews for a specific product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reviews retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(
            @Parameter(description = "Product ID to get reviews for", required = true) @PathVariable Long productId) {

        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);

    }

    @Operation(summary = "Create product review", description = "Create a new review for a product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid review data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> writeReview(
            @Parameter(description = "Review creation request data", required = true) @RequestBody CreateReviewRequest req,
            @Parameter(description = "Product ID to review", required = true) @PathVariable Long productId,
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt) throws UserException, ProductException {

        User user = userService.findUserProfileByJwt(jwt);
        Product product = productService.findProductById(productId);

        Review review = reviewService.createReview(
                req, user, product
        );
        return ResponseEntity.ok(review);

    }

    @Operation(summary = "Update review", description = "Update an existing review (only by the review author)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid review data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not the review author"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @Parameter(description = "Updated review data", required = true) @RequestBody CreateReviewRequest req,
            @Parameter(description = "Review ID to update", required = true) @PathVariable Long reviewId,
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt)
            throws UserException,
            ReviewNotFoundException, AuthenticationException {

        User user = userService.findUserProfileByJwt(jwt);

        Review review = reviewService.updateReview(
                reviewId,
                req.getReviewText(),
                req.getReviewRating(),
                user.getId()
        );
        return ResponseEntity.ok(review);

    }

    @Operation(summary = "Delete review", description = "Delete an existing review (only by the review author)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not the review author"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @Parameter(description = "Review ID to delete", required = true) @PathVariable Long reviewId,
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt) throws UserException,
            ReviewNotFoundException, AuthenticationException {

        User user = userService.findUserProfileByJwt(jwt);

        reviewService.deleteReview(reviewId, user.getId());
        ApiResponse res = new ApiResponse();
        res.setMessage("Review deleted successfully");
        res.setStatus(true);

        return ResponseEntity.ok(res);

    }
}

package com.zosh.controller;

import com.zosh.exception.CategoryNotFoundException;
import com.zosh.exception.ProductException;
import com.zosh.exception.SellerException;
import com.zosh.exception.UserException;
import com.zosh.model.Product;
import com.zosh.model.Seller;
import com.zosh.request.CreateProductRequest;
import com.zosh.service.ProductService;
import com.zosh.service.SellerService;
import com.zosh.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers/product")
@RequiredArgsConstructor
@Tag(name = "Seller Products", description = "Seller product management APIs for creating, updating, and managing products")
public class SellerProductController {

    private final ProductService productService;
    private final SellerService sellerService;
    private final UserService userService;


    @Operation(summary = "Get seller products", description = "Retrieve all products for the authenticated seller")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Seller authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping()
    public ResponseEntity<List<Product>> getProductBySellerId(
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt) throws ProductException, SellerException {

        Seller seller=sellerService.getSellerProfile(jwt);

        List<Product> products = productService.getProductBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    @Operation(summary = "Create new product", description = "Create a new product for the authenticated seller")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Product created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid product data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Seller authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping()
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Product creation request data", required = true) @RequestBody CreateProductRequest request,
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization")String jwt)
            throws UserException,
            ProductException, CategoryNotFoundException, SellerException {

        Seller seller=sellerService.getSellerProfile(jwt);

        Product product = productService.createProduct(request, seller);
        return new ResponseEntity<>(product, HttpStatus.CREATED);

    }

    @Operation(summary = "Delete product", description = "Delete a specific product by ID (Seller only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Seller authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID to delete", required = true) @PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update product", description = "Update an existing product by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid product data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Seller authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product ID to update", required = true) @PathVariable Long productId, 
            @Parameter(description = "Updated product data", required = true) @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(productId, product);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update product stock", description = "Update the stock status of a specific product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product stock updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Seller authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Product> updateProductStock(
            @Parameter(description = "Product ID to update stock for", required = true) @PathVariable Long productId) {
        try {
            Product updatedProduct = productService.updateProductStock(productId);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}

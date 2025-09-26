package com.zosh.controller;

import com.zosh.exception.*;
import com.zosh.model.*;
import com.zosh.request.CreateProductRequest;
import com.zosh.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {


    private final ProductService productService;

    private final UserService userService;

    private final SellerService sellerService;



    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "Product ID", required = true) @PathVariable Long productId) throws ProductException {

            Product product = productService.findProductById(productId);
            return new ResponseEntity<>(product, HttpStatus.OK);

    }

    @Operation(summary = "Search products", description = "Search products by query string")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(
            @Parameter(description = "Search query") @RequestParam(required = false) String query) {
        List<Product> products = productService.searchProduct(query);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Operation(summary = "Get all products", description = "Retrieve all products with optional filtering and pagination")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @Parameter(description = "Filter by category") @RequestParam(required = false) String category,
            @Parameter(description = "Filter by brand") @RequestParam(required = false) String brand,
            @Parameter(description = "Filter by color") @RequestParam(required = false) String color,
            @Parameter(description = "Filter by size") @RequestParam(required = false) String size,
            @Parameter(description = "Minimum price filter") @RequestParam(required = false) Integer minPrice,
            @Parameter(description = "Maximum price filter") @RequestParam(required = false) Integer maxPrice,
            @Parameter(description = "Minimum discount filter") @RequestParam(required = false) Integer minDiscount,
            @Parameter(description = "Sort order (price_low, price_high, etc.)") @RequestParam(required = false) String sort,
            @Parameter(description = "Stock availability") @RequestParam(required = false) String stock,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") Integer pageNumber) {
        System.out.println("color p -------- "+pageNumber);
        return new ResponseEntity<>(
                productService.getAllProduct(category,brand,
                        color, size, minPrice,
                        maxPrice, minDiscount, sort,
                        stock, pageNumber), HttpStatus.OK);
    }
}

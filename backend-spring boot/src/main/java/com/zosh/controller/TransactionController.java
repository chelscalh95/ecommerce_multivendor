package com.zosh.controller;

import com.zosh.exception.SellerException;
import com.zosh.model.Order;
import com.zosh.model.Seller;
import com.zosh.model.Transaction;
import com.zosh.service.SellerService;
import com.zosh.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Transaction management APIs for tracking payment transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final SellerService sellerService;

    @Autowired
    public TransactionController(TransactionService transactionService, SellerService sellerService) {
        this.transactionService = transactionService;
        this.sellerService = sellerService;
    }

    @Operation(summary = "Create transaction", description = "Create a new transaction for an order")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid order data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @Parameter(description = "Order data for transaction creation", required = true) @RequestBody Order order) {
        Transaction transaction = transactionService.createTransaction(order);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Get seller transactions", description = "Retrieve all transactions for the authenticated seller")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Seller authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionBySeller(
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt) throws SellerException {
        Seller seller=sellerService.getSellerProfile(jwt);

        List<Transaction> transactions = transactionService.getTransactionBySeller(seller);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get all transactions", description = "Retrieve all transactions in the system (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "All transactions retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Admin access required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}

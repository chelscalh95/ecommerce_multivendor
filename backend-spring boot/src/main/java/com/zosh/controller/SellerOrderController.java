package com.zosh.controller;

import com.zosh.domain.OrderStatus;
import com.zosh.exception.OrderException;
import com.zosh.exception.SellerException;
import com.zosh.model.Order;
import com.zosh.model.Seller;
import com.zosh.response.ApiResponse;
import com.zosh.service.OrderService;
import com.zosh.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller/orders")
@Tag(name = "Seller Orders", description = "Seller order management APIs for viewing and managing orders")
public class SellerOrderController {

    private final OrderService orderService;

    private final SellerService sellerService;

    @Autowired
    public SellerOrderController(OrderService orderService,

                                 SellerService sellerService) {
        this.orderService=orderService;


        this.sellerService = sellerService;
    }

    @Operation(summary = "Get all seller orders", description = "Retrieve all orders for the authenticated seller")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Orders retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Seller authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seller not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersHandler(
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt
    ) throws SellerException {
        Seller seller=sellerService.getSellerProfile(jwt);
        List<Order> orders=orderService.getShopsOrders(seller.getId());

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Update order status", description = "Update the status of a specific order (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Order status updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid order status"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Seller authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Order ID to update", required = true) @PathVariable Long orderId,
            @Parameter(description = "New order status", required = true) @PathVariable OrderStatus orderStatus
    ) throws OrderException {

        Order orders=orderService.updateOrderStatus(orderId,orderStatus);

        return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
    }


    @Operation(summary = "Delete order", description = "Delete a specific order (Seller only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Order deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Seller authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrderHandler(
            @Parameter(description = "Order ID to delete", required = true) @PathVariable Long orderId,
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt) throws OrderException{
        orderService.deleteOrder(orderId);
        ApiResponse res=new ApiResponse("Order Deleted Successfully",true);
        System.out.println("delete method working....");
        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }

}

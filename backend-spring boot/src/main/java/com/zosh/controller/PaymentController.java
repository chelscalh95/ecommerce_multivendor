package com.zosh.controller;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.zosh.domain.PaymentMethod;
import com.zosh.exception.UserException;
import com.zosh.model.*;
import com.zosh.repository.CartItemRepository;
import com.zosh.repository.CartRepository;
import com.zosh.response.ApiResponse;
import com.zosh.response.PaymentLinkResponse;
import com.zosh.service.*;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment processing APIs for handling payment links and payment confirmations")
public class PaymentController {

    private final UserService userService;
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final SellerReportService sellerReportService;
    private final SellerService sellerService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    @Operation(summary = "Create payment link", description = "Create a payment link for an order using specified payment method")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Payment link created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentLinkResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payment method or order ID"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error or payment service error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/api/payment/{paymentMethod}/order/{orderId}")
    public ResponseEntity<PaymentLinkResponse> paymentHandler(
            @Parameter(description = "Payment method (RAZORPAY or STRIPE)", required = true) @PathVariable PaymentMethod paymentMethod,
            @Parameter(description = "Order ID for payment", required = true) @PathVariable Long orderId,
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentLinkResponse paymentResponse;

        PaymentOrder order= paymentService.getPaymentOrderById(orderId);

//        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
//            paymentResponse=paymentService.createRazorpayPaymentLink(user,
//                    order.getAmount(),
//                    order.getId());
//        }
//        else{
//            paymentResponse=paymentService.createStripePaymentLink(user,
//                    order.getAmount(),
//                    order.getId());
//        }

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }


    @Operation(summary = "Handle payment success", description = "Process successful payment and update order status, seller reports, and clear cart")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Payment processed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payment ID or payment link ID"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Payment order not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error or payment processing failed")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/api/payment/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @Parameter(description = "Payment ID from payment gateway", required = true) @PathVariable String paymentId,
            @Parameter(description = "Payment link ID", required = true) @RequestParam String paymentLinkId,
            @Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentLinkResponse paymentResponse;

        PaymentOrder paymentOrder= paymentService
                .getPaymentOrderByPaymentId(paymentLinkId);

        boolean paymentSuccess = paymentService.ProceedPaymentOrder(
                paymentOrder,
                paymentId,
                paymentLinkId
        );
        if(paymentSuccess){
            for(Order order:paymentOrder.getOrders()){
                transactionService.createTransaction(order);
                Seller seller=sellerService.getSellerById(order.getSellerId());
                SellerReport report=sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders()+1);
                report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }
            Cart cart=cartRepository.findByUserId(user.getId());
            cart.setCouponPrice(0);
            cart.setCouponCode(null);
//        Set<CartItem> items=cart.getCartItems();
//        cartItemRepository.deleteAll(items);
//        cart.setCartItems(new HashSet<>());
            cartRepository.save(cart);

        }
      
        ApiResponse res = new ApiResponse();
        res.setMessage("Payment successful");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}

package com.zosh.controller;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.zosh.domain.PaymentMethod;
import com.zosh.exception.OrderException;
import com.zosh.exception.SellerException;
import com.zosh.exception.UserException;
import com.zosh.model.*;
import com.zosh.repository.PaymentOrderRepository;
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

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management APIs for creating, tracking, and managing customer orders")
public class OrderController {
	
	private final OrderService orderService;
	private final UserService userService;
	private final OrderItemService orderItemService;
	private final CartService cartService;
	private final PaymentService paymentService;
	private final PaymentOrderRepository paymentOrderRepository;
	private final SellerReportService sellerReportService;
	private final SellerService sellerService;

	
	@Operation(summary = "Create new order", description = "Create a new order with shipping address and payment method, returns payment link")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order created successfully with payment link",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentLinkResponse.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid address or payment method"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error or payment processing failed")
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping()
	public ResponseEntity<PaymentLinkResponse> createOrderHandler(
			@Parameter(description = "Shipping address for the order", required = true) @RequestBody Address spippingAddress,
			@Parameter(description = "Payment method (RAZORPAY or STRIPE)", required = true) @RequestParam PaymentMethod paymentMethod,
			@Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization")String jwt)
            throws UserException, RazorpayException, StripeException {
		
		User user=userService.findUserProfileByJwt(jwt);
		Cart cart=cartService.findUserCart(user);
		Set<Order> orders =orderService.createOrder(user, spippingAddress,cart);

		PaymentOrder paymentOrder=paymentService.createOrder(user,orders);

		PaymentLinkResponse res = new PaymentLinkResponse();

		if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
			PaymentLink payment=paymentService.createRazorpayPaymentLink(user,
					paymentOrder.getAmount(),
					paymentOrder.getId());
			String paymentUrl=payment.get("short_url");
			String paymentUrlId=payment.get("id");


			res.setPayment_link_url(paymentUrl);
//			res.setPayment_link_id(paymentUrlId);
			paymentOrder.setPaymentLinkId(paymentUrlId);
			paymentOrderRepository.save(paymentOrder);
		}
		else{
			String paymentUrl=paymentService.createStripePaymentLink(user,
					paymentOrder.getAmount(),
					paymentOrder.getId());
			res.setPayment_link_url(paymentUrl);
		}
		return new ResponseEntity<>(res,HttpStatus.OK);

	}
	
	@Operation(summary = "Get user order history", description = "Retrieve all orders for the authenticated user")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Order history retrieved successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/user")
	public ResponseEntity< List<Order>> usersOrderHistoryHandler(
			@Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization")
	String jwt) throws UserException{
		
		User user=userService.findUserProfileByJwt(jwt);
		List<Order> orders=orderService.usersOrderHistory(user.getId());
		return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
	}
	
	@Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Order retrieved successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{orderId}")
	public ResponseEntity< Order> getOrderById(
			@Parameter(description = "Order ID", required = true) @PathVariable Long orderId, 
			@Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization")
	String jwt) throws OrderException, UserException{
		
		User user = userService.findUserProfileByJwt(jwt);
		Order orders=orderService.findOrderById(orderId);
		return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Get order item by ID", description = "Retrieve a specific order item by its ID")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Order item retrieved successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order item not found"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/item/{orderItemId}")
	public ResponseEntity<OrderItem> getOrderItemById(
			@Parameter(description = "Order item ID", required = true) @PathVariable Long orderItemId, 
			@Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization")
	String jwt) throws Exception {
		System.out.println("------- controller ");
		User user = userService.findUserProfileByJwt(jwt);
		OrderItem orderItem=orderItemService.getOrderItemById(orderItemId);
		return new ResponseEntity<>(orderItem,HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Cancel order", description = "Cancel an existing order and update seller reports")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order cancelled successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Order cannot be cancelled"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Order> cancelOrder(
			@Parameter(description = "Order ID to cancel", required = true) @PathVariable Long orderId,
			@Parameter(description = "JWT Authorization token", required = true) @RequestHeader("Authorization") String jwt
	) throws UserException, OrderException, SellerException {
		User user=userService.findUserProfileByJwt(jwt);
		Order order=orderService.cancelOrder(orderId,user);

		Seller seller= sellerService.getSellerById(order.getSellerId());
		SellerReport report=sellerReportService.getSellerReport(seller);

		report.setCanceledOrders(report.getCanceledOrders()+1);
		report.setTotalRefunds(report.getTotalRefunds()+order.getTotalSellingPrice());
		sellerReportService.updateSellerReport(report);

		return ResponseEntity.ok(order);
	}

}

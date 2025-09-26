package com.ecom.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecom.exception.CartItemException;
import com.ecom.exception.UserException;
import com.ecom.model.CartItem;
import com.ecom.model.User;
import com.ecom.response.ApiResponse;
import com.ecom.service.CartItemService;
import com.ecom.service.UserService;

@RestController
@RequestMapping("/api/cart_items")
@Tag(name = "Cart Items", description = "Cart item management APIs - Currently no endpoints implemented")
public class CartItemController {

	private CartItemService cartItemService;
	private UserService userService;
	
	public CartItemController(CartItemService cartItemService, UserService userService) {
		this.cartItemService=cartItemService;
		this.userService=userService;
	}
	

}

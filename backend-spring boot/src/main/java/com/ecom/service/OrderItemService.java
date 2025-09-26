package com.ecom.service;


import com.ecom.exception.OrderException;
import com.ecom.model.OrderItem;
import com.ecom.model.Product;

public interface OrderItemService {

	OrderItem getOrderItemById(Long id) throws Exception;
	


}

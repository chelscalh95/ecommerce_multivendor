package com.ecom.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User roles in the e-commerce platform")
public enum USER_ROLE {
	
	@Schema(description = "Customer role - can browse and purchase products")
	ROLE_CUSTOMER,
	
	@Schema(description = "Seller role - can manage products and orders")
	ROLE_SELLER,
	
	@Schema(description = "Admin role - has full system access")
	ROLE_ADMIN

}
-- Sample data for User
INSERT INTO user (id, password, email, full_name, mobile, role) VALUES
  (1, 'password1', 'user1@example.com', 'User One', '1234567890', 'CUSTOMER'),
  (2, 'password2', 'user2@example.com', 'User Two', '0987654321', 'CUSTOMER');

INSERT INTO user (id, password, email, full_name, mobile, role) VALUES
  (3, 'password3', 'user3@example.com', 'User Three', '1112223333', 'CUSTOMER'),
  (4, 'password4', 'user4@example.com', 'User Four', '4445556666', 'SELLER'),
  (5, 'password5', 'user5@example.com', 'User Five', '7778889999', 'ADMIN');

-- Sample data for Address
INSERT INTO address (id, name, locality, address, city, state, pin_code, mobile) VALUES
  (1, 'Home', 'Locality1', '123 Main St', 'CityA', 'StateA', '111111', '1234567890'),
  (2, 'Office', 'Locality2', '456 Office Rd', 'CityB', 'StateB', '222222', '0987654321');

INSERT INTO address (id, name, locality, address, city, state, pin_code, mobile) VALUES
  (3, 'Warehouse', 'Locality3', '789 Warehouse Ave', 'CityC', 'StateC', '333333', '1112223333'),
  (4, 'Shop', 'Locality4', '101 Shop Blvd', 'CityD', 'StateD', '444444', '4445556666');

-- Sample data for Category
INSERT INTO categories (id, name, category_id, parent_category_id, level) VALUES
  (1, 'Electronics', 'ELEC', NULL, 1),
  (2, 'Mobiles', 'MOB', 1, 2);

INSERT INTO categories (id, name, category_id, parent_category_id, level) VALUES
  (3, 'Fashion', 'FASH', NULL, 1),
  (4, 'Men', 'MEN', 3, 2),
  (5, 'Women', 'WOMEN', 3, 2);

-- Sample data for Seller
INSERT INTO seller (id, seller_name, mobile, email, password, GSTIN, role, is_email_verified, account_status) VALUES
  (1, 'Seller One', '1112223333', 'seller1@example.com', 'sellerpass', 'GSTIN123', 'SELLER', true, 'VERIFIED');

INSERT INTO seller (id, seller_name, mobile, email, password, GSTIN, role, is_email_verified, account_status) VALUES
  (2, 'Seller Two', '2223334444', 'seller2@example.com', 'sellerpass2', 'GSTIN456', 'SELLER', false, 'PENDING_VERIFICATION');

-- Sample data for Product
INSERT INTO product (id, title, description, mrp_price, selling_price, discount_percent, quantity, color, num_ratings, category_id, seller_id, created_at, sizes) VALUES
  (1, 'Smartphone', 'Latest smartphone', 50000, 45000, 10, 100, 'Black', 0, 2, 1, NOW(), 'M,L,XL');

INSERT INTO product (id, title, description, mrp_price, selling_price, discount_percent, quantity, color, num_ratings, category_id, seller_id, created_at, sizes) VALUES
  (2, 'Laptop', 'Gaming laptop', 100000, 90000, 10, 50, 'Gray', 0, 1, 2, NOW(), '15,17'),
  (3, 'T-Shirt', 'Cotton T-shirt', 1000, 800, 20, 200, 'Blue', 0, 4, 1, NOW(), 'S,M,L'),
  (4, 'Dress', 'Summer dress', 2000, 1500, 25, 150, 'Red', 0, 5, 2, NOW(), 'M,L,XL');

-- Sample data for Coupon
INSERT INTO coupon (id, code, discount_percentage, validity_start_date, validity_end_date, minimum_order_value, is_active) VALUES
  (1, 'WELCOME10', 10, '2025-01-01', '2025-12-31', 1000, true);

INSERT INTO coupon (id, code, discount_percentage, validity_start_date, validity_end_date, minimum_order_value, is_active) VALUES
  (2, 'FESTIVE20', 20, '2025-08-01', '2025-08-31', 2000, true),
  (3, 'SUMMER5', 5, '2025-06-01', '2025-09-01', 500, false);

-- Sample data for Wishlist
INSERT INTO wishlist (id, user_id) VALUES (1, 1);

INSERT INTO wishlist (id, user_id) VALUES (2, 2);

-- Sample data for Wishlist Products
INSERT INTO wishlist_products (wishlist_id, products_id) VALUES (1, 1);

INSERT INTO wishlist_products (wishlist_id, products_id) VALUES (1, 2), (2, 3), (2, 4);

-- Sample data for Order
INSERT INTO orders (id, order_id, user_id, seller_id, shipping_address_id, total_mrp_price, total_selling_price, discount, order_status) VALUES
  (1, 'ORD123', 1, 1, 1, 50000, 45000, 5000, 'PLACED');

INSERT INTO orders (id, order_id, user_id, seller_id, shipping_address_id, total_mrp_price, total_selling_price, discount, order_status) VALUES
  (2, 'ORD124', 2, 2, 2, 100000, 90000, 10000, 'SHIPPED'),
  (3, 'ORD125', 3, 1, 3, 2000, 1500, 500, 'DELIVERED');

-- Sample data for OrderItem
INSERT INTO order_item (id, order_id, product_id, size, quantity, mrp_price, selling_price) VALUES
  (1, 1, 1, 'M', 1, 50000, 45000);

INSERT INTO order_item (id, order_id, product_id, size, quantity, mrp_price, selling_price) VALUES
  (2, 2, 2, '15', 1, 100000, 90000),
  (3, 3, 4, 'L', 2, 2000, 1500);

-- Sample data for Transaction
INSERT INTO transaction (id, customer_id, order_id, seller_id, date) VALUES
  (1, 1, 1, 1, NOW());

INSERT INTO transaction (id, customer_id, order_id, seller_id, date) VALUES
  (2, 2, 2, 2, NOW()),
  (3, 3, 3, 1, NOW());

-- Sample data for Review
INSERT INTO review (id, review_text, rating, product_id, user_id) VALUES
  (1, 'Great product!', 5, 1, 1);

INSERT INTO review (id, review_text, rating, product_id, user_id) VALUES
  (2, 'Good value for money.', 4, 2, 2),
  (3, 'Comfortable and stylish.', 5, 3, 3),
  (4, 'Nice color and fit.', 4, 4, 2);

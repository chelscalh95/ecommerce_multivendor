-- Use ecommercemultivendor database
USE ecommercemultivendor;

-- Clear existing data (in reverse order of dependencies)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE review_product_images;
TRUNCATE TABLE review;
TRUNCATE TABLE transaction;
TRUNCATE TABLE order_item;
TRUNCATE TABLE orders;
TRUNCATE TABLE payment_order;
TRUNCATE TABLE wishlist_products;
TRUNCATE TABLE wishlist;
TRUNCATE TABLE cart_item;
TRUNCATE TABLE cart;
TRUNCATE TABLE user_coupons;
TRUNCATE TABLE coupon;
TRUNCATE TABLE product_images;
TRUNCATE TABLE product;
TRUNCATE TABLE seller_report;
TRUNCATE TABLE seller;
TRUNCATE TABLE home_category;
TRUNCATE TABLE deal;
TRUNCATE TABLE categories;
TRUNCATE TABLE notification;
TRUNCATE TABLE password_reset_token;
TRUNCATE TABLE verification_code;
TRUNCATE TABLE user_addresses;
TRUNCATE TABLE address;
TRUNCATE TABLE user;
SET FOREIGN_KEY_CHECKS = 1;

-- Sample data for User (Customers)
INSERT INTO user (id, password, email, full_name, mobile, role) VALUES
  (1, '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', 'john.doe@example.com', 'John Doe', '9876543210', 'CUSTOMER'),
  (2, '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', 'jane.smith@example.com', 'Jane Smith', '9876543211', 'CUSTOMER'),
  (3, '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', 'robert.brown@example.com', 'Robert Brown', '9876543212', 'CUSTOMER'),
  (4, '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', 'emily.davis@example.com', 'Emily Davis', '9876543213', 'CUSTOMER'),
  (5, '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', 'michael.wilson@example.com', 'Michael Wilson', '9876543214', 'CUSTOMER'),
  (6, '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', 'admin@example.com', 'System Admin', '9876543215', 'ADMIN');

-- Sample data for Address
INSERT INTO address (id, name, locality, address, city, state, pin_code, mobile) VALUES
  (1, 'John Home', 'Indiranagar', '123 Main Street, Apt 4B', 'Bangalore', 'Karnataka', '560038', '9876543210'),
  (2, 'John Office', 'Koramangala', '456 Tech Park, Building 2', 'Bangalore', 'Karnataka', '560034', '9876543210'),
  (3, 'Jane Home', 'Bandra', '789 Sea View Apartments', 'Mumbai', 'Maharashtra', '400050', '9876543211'),
  (4, 'Warehouse Mumbai', 'Andheri', 'Industrial Area, Warehouse 5', 'Mumbai', 'Maharashtra', '400059', '9876543220'),
  (5, 'Fashion Store Delhi', 'Connaught Place', 'CP Mall, Shop 23', 'New Delhi', 'Delhi', '110001', '9876543221'),
  (6, 'Electronics Hub', 'Nehru Place', 'Electronics Market, Block B', 'New Delhi', 'Delhi', '110019', '9876543222'),
  (7, 'Robert Home', 'Salt Lake', '45 Park Street', 'Kolkata', 'West Bengal', '700091', '9876543212'),
  (8, 'Emily Residence', 'T. Nagar', '67 Anna Salai', 'Chennai', 'Tamil Nadu', '600017', '9876543213');

-- Link addresses to users
INSERT INTO user_addresses (user_id, addresses_id) VALUES
  (1, 1), (1, 2), (2, 3), (3, 7), (4, 8);

-- Sample data for Categories (Hierarchical)
INSERT INTO categories (id, name, category_id, parent_category_id, level) VALUES
  -- Top Level Categories
  (1, 'Electronics', 'ELECTRONICS', NULL, 1),
  (2, 'Fashion', 'FASHION', NULL, 1),
  (3, 'Home & Furniture', 'HOME_FURNITURE', NULL, 1),
  (4, 'Beauty & Personal Care', 'BEAUTY', NULL, 1),
  
  -- Electronics Sub-categories
  (5, 'Mobile Phones', 'MOBILE_PHONES', 1, 2),
  (6, 'Laptops', 'LAPTOPS', 1, 2),
  (7, 'Smart Watches', 'SMART_WATCHES', 1, 2),
  
  -- Fashion Sub-categories
  (8, 'Men', 'MEN', 2, 2),
  (9, 'Women', 'WOMEN', 2, 2),
  (10, 'Kids', 'KIDS', 2, 2),
  
  -- Men's Fashion Sub-categories
  (11, 'Men Shirts', 'MEN_SHIRTS', 8, 3),
  (12, 'Men T-Shirts', 'MEN_TSHIRTS', 8, 3),
  
  -- Women's Fashion Sub-categories
  (13, 'Sarees', 'SAREES', 9, 3),
  (14, 'Dresses', 'DRESSES', 9, 3),
  
  -- Furniture Sub-categories
  (15, 'Living Room', 'LIVING_ROOM', 3, 2),
  (16, 'Bedroom', 'BEDROOM', 3, 2);

-- Sample data for Seller
INSERT INTO seller (id, seller_name, mobile, email, password, gstin, is_email_verified, account_status, business_name, business_email, business_mobile, business_address, logo, banner, account_number, account_holder_name, ifsc_code, pickup_address_id) VALUES
  (1, 'TechWorld Electronics', '9876543220', 'seller@techworld.com', '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', '27AABCU9603R1ZM', true, 'ACTIVE', 'TechWorld Electronics Pvt Ltd', 'business@techworld.com', '9876543220', 'Electronics Market, Nehru Place, Delhi', 'assets/images/white-male-1834125_1920.jpg', 'assets/images/seller_banner_image.jpg', '1234567890123456', 'TechWorld Electronics', 'HDFC0001234', 6),
  
  (2, 'Fashion Fiesta', '9876543221', 'seller@fashionfiesta.com', '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', '18AABCT1234K1ZN', true, 'ACTIVE', 'Fashion Fiesta Clothing', 'business@fashionfiesta.com', '9876543221', 'CP Mall, Connaught Place, Delhi', 'assets/images/white-male-1834125_1920.jpg', 'assets/images/seller_banner_image.jpg', '9876543210987654', 'Fashion Fiesta', 'ICIC0001234', 5),
  
  (3, 'Home Decor Hub', '9876543222', 'seller@homedecor.com', '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', '09AABCP1234M1ZP', true, 'ACTIVE', 'Home Decor Hub Ltd', 'business@homedecor.com', '9876543222', 'Industrial Area, Mumbai', 'assets/images/white-male-1834125_1920.jpg', 'assets/images/seller_banner_image.jpg', '5432167890123456', 'Home Decor Hub', 'SBIN0001234', 4);

-- Sample data for Products with actual image paths
INSERT INTO product (id, title, description, mrp_price, selling_price, discount_percent, quantity, color, num_ratings, category_id, seller_id, created_at, sizes, in_stock) VALUES
  -- Mobile Phones
  (1, 'iPhone 15 Pro Max 256GB', 'Latest iPhone with A17 Pro chip, titanium design, and advanced camera system', 159900, 149900, 6, 50, 'Natural Titanium', 125, 5, 1, NOW(), NULL, true),
  (2, 'Samsung Galaxy S24 Ultra', '6.8" Dynamic AMOLED 2X display, S Pen, 200MP camera', 134999, 124999, 7, 40, 'Titanium Gray', 89, 5, 1, NOW(), NULL, true),
  
  -- Smart Watches
  (3, 'Apple Watch Series 9', 'Advanced health features, fitness tracking, and seamless iPhone integration', 49900, 44900, 10, 75, 'Midnight', 200, 7, 1, NOW(), NULL, true),
  (4, 'Samsung Galaxy Watch 6', 'Wear OS powered smartwatch with advanced health monitoring', 34999, 29999, 14, 60, 'Graphite', 150, 7, 1, NOW(), NULL, true),
  
  -- Men's Shirts
  (5, 'Louis Philippe Formal Shirt', 'Premium cotton formal shirt with classic fit', 2999, 1799, 40, 100, 'White', 45, 11, 2, NOW(), 'S,M,L,XL,XXL', true),
  (6, 'Louis Philippe Casual Shirt', 'Comfortable casual shirt perfect for weekend outings', 2599, 1599, 38, 80, 'Blue', 38, 11, 2, NOW(), 'S,M,L,XL', true),
  
  -- Men's T-Shirts
  (7, 'Nike Dri-FIT T-Shirt', 'Moisture-wicking fabric keeps you dry and comfortable', 1995, 1495, 25, 120, 'Black', 67, 12, 2, NOW(), 'S,M,L,XL', true),
  (8, 'Adidas Performance Tee', 'Lightweight and breathable sports t-shirt', 1799, 1299, 28, 150, 'Navy Blue', 82, 12, 2, NOW(), 'S,M,L,XL,XXL', true),
  
  -- Sarees
  (9, 'Banarasi Silk Saree', 'Traditional handwoven Banarasi silk saree with intricate zari work', 15999, 11999, 25, 30, 'Red', 125, 13, 2, NOW(), NULL, true),
  (10, 'Kanjivaram Silk Saree', 'Authentic Kanjivaram silk saree with temple border', 18999, 14999, 21, 25, 'Green', 98, 13, 2, NOW(), NULL, true),
  (11, 'Chiffon Designer Saree', 'Elegant pure chiffon saree with embroidered border', 4999, 3499, 30, 40, 'Pink', 156, 13, 2, NOW(), NULL, true),
  (12, 'Georgette Party Wear Saree', 'Stylish georgette saree perfect for parties and occasions', 3999, 2799, 30, 45, 'Blue', 134, 13, 2, NOW(), NULL, true),
  
  -- Furniture
  (13, 'Modern Sofa Set 3+1+1', 'Comfortable modern sofa set with premium fabric upholstery', 45999, 35999, 22, 15, 'Grey', 28, 15, 3, NOW(), NULL, true),
  (14, 'Wooden Dining Table 6 Seater', 'Solid wood dining table with 6 chairs', 38999, 29999, 23, 10, 'Walnut', 35, 15, 3, NOW(), NULL, true),
  (15, 'King Size Bed with Storage', 'Engineered wood king size bed with hydraulic storage', 32999, 25999, 21, 20, 'Dark Brown', 42, 16, 3, NOW(), NULL, true);

-- Add product images
INSERT INTO product_images (product_id, images) VALUES
  -- iPhones
  (1, 'assets/images/mobiel/-original-imagx9egdetafafz.webp'),
  (1, 'assets/images/mobiel/-original-imagx9eghmmcbpup.webp'),
  (1, 'assets/images/mobiel/-original-imagx9egkfqux8qh.webp'),
  (1, 'assets/images/mobiel/-original-imagx9egvraqxfeh.webp'),
  
  -- Samsung phones
  (2, 'assets/images/mobiel/-original-imagx9egwjgtgwqf.webp'),
  (2, 'assets/images/mobiel/-original-imagx9egxcyjzvux.webp'),
  (2, 'assets/images/mobiel/-original-imagx9pf7dd5ny7n.webp'),
  (2, 'assets/images/mobiel/-original-imagx9pfdevtsjey.webp'),
  
  -- Smart watches
  (3, 'assets/images/watch/android-ios-90172ap01-titan-yes-original-imagqggzvzabrhmv 1.webp'),
  (3, 'assets/images/watch/android-ios-90172ap01-titan-yes-original-imagqggzvzabrhmv 2.webp'),
  (3, 'assets/images/watch/android-ios-90172ap01-titan-yes-original-imagqggzvzabrhmv 3.webp'),
  
  (4, 'assets/images/watch/pro-ray-android-ios-cellecor-yes-original-imagydnsrany7qhy 1.webp'),
  (4, 'assets/images/watch/pro-ray-android-ios-cellecor-yes-original-imagydnsrany7qhy 2.webp'),
  (4, 'assets/images/watch/pro-ray-android-ios-cellecor-yes-original-imagydnsrany7qhy 3.webp'),
  
  -- Men's shirts
  (5, 'assets/images/Men shirt/Louis-Philippe-Men-Shirts 1.jpg'),
  (5, 'assets/images/Men shirt/Louis-Philippe-Men-Shirts 2.jpg'),
  (5, 'assets/images/Men shirt/Louis-Philippe-Men-Shirts 3.jpg'),
  (5, 'assets/images/Men shirt/Louis-Philippe-Men-Shirts 4.jpg'),
  
  (6, 'assets/images/Men shirt/Louis-Philippe-Men-Shirts 1.jpg'),
  (6, 'assets/images/Men shirt/Louis-Philippe-Men-Shirts 2.jpg'),
  
  -- Men's t-shirts
  (7, 'assets/images/men tshirt/4QdHw1UN_f8db19fa1b1947689b2cc1f461b25b14.jpg'),
  (7, 'assets/images/men tshirt/6ip1jSbB_f6fe477ff55a4f9fa3f929f3c2d28ad9.jpg'),
  
  (8, 'assets/images/men tshirt/SQpixTpY_d1ffd40185c94a39b5ca656a8c8d5d00.jpg'),
  (8, 'assets/images/men tshirt/6ip1jSbB_f6fe477ff55a4f9fa3f929f3c2d28ad9.jpg'),
  
  -- Sarees
  (9, 'assets/images/products/banarasi-saree 1.jpg'),
  (9, 'assets/images/products/banarasi-saree 2.webp'),
  (9, 'assets/images/products/banarasi-saree 3.webp'),
  
  (10, 'assets/images/products/1aedb64c-c2f6-4378-8e9f-26331cd12bf81715321471296-Tankori-Floral-Zari-Tissue-Kanjeevaram-Saree-405171532147109-6.jpg'),
  (10, 'assets/images/products/347cf3c8-1761-4232-b45e-ce752fc699651715321471352-Tankori-Floral-Zari-Tissue-Kanjeevaram-Saree-405171532147109-2.jpg'),
  (10, 'assets/images/products/45b98a98-c166-409b-b1d5-ad3fc3139ee71715321471338-Tankori-Floral-Zari-Tissue-Kanjeevaram-Saree-405171532147109-3.jpg'),
  (10, 'assets/images/products/8c13ada1-9959-4e42-ab52-abd6bd9067931715321471281-Tankori-Floral-Zari-Tissue-Kanjeevaram-Saree-405171532147109-7.jpg'),
  
  (11, 'assets/images/products/PureChiffonSaree 1.jpg'),
  (11, 'assets/images/products/PureChiffonSaree 2.jpg'),
  (11, 'assets/images/products/PureChiffonSaree 3.jpg'),
  (11, 'assets/images/products/PureChiffonSaree 4.jpg'),
  
  (12, 'assets/images/products/TheTextileHubFloralPolyGeorgetteSaree 1.jpg'),
  (12, 'assets/images/products/TheTextileHubFloralPolyGeorgetteSaree 2.jpg'),
  (12, 'assets/images/products/TheTextileHubFloralPolyGeorgetteSaree 3.jpg'),
  (12, 'assets/images/products/TheTextileHubFloralPolyGeorgetteSaree 4.jpg'),
  
  -- Furniture
  (13, 'assets/images/furniture/Runners 1.jpg'),
  (13, 'assets/images/furniture/Runners 2.jpg'),
  (13, 'assets/images/furniture/Runners 3.jpg'),
  
  (14, 'assets/images/furniture/Runners 1.jpg'),
  (14, 'assets/images/furniture/Runners 2.jpg'),
  
  (15, 'assets/images/furniture/Runners 3.jpg'),
  (15, 'assets/images/furniture/Runners 1.jpg');

-- Sample data for Coupons
INSERT INTO coupon (id, code, discount_percentage, validity_start_date, validity_end_date, minimum_order_value, is_active) VALUES
  (1, 'WELCOME20', 20, '2025-01-01', '2025-12-31', 1000, true),
  (2, 'FASHION15', 15, '2025-01-01', '2025-06-30', 2000, true),
  (3, 'TECH10', 10, '2025-01-01', '2025-12-31', 5000, true),
  (4, 'DIWALI25', 25, '2025-10-01', '2025-11-15', 3000, false),
  (5, 'SUMMER30', 30, '2025-04-01', '2025-06-30', 2500, true);

-- Link coupons to users
INSERT INTO user_coupons (user_id, coupons_id) VALUES
  (1, 1), (1, 2), (1, 3),
  (2, 1), (2, 4),
  (3, 2), (3, 5),
  (4, 1), (4, 3);

-- Sample data for Cart
INSERT INTO cart (id, user_id, total_selling_price, total_item, total_mrp_price, discount, coupon_code, coupon_price) VALUES
  (1, 1, 1799, 1, 2999, 1200, 'FASHION15', 270),
  (2, 2, 149900, 1, 159900, 10000, NULL, 0),
  (3, 3, 5598, 2, 7998, 2400, 'FASHION15', 840);

-- Sample data for Cart Items
INSERT INTO cart_item (id, cart_id, product_id, size, quantity, mrp_price, selling_price, user_id) VALUES
  (1, 1, 5, 'L', 1, 2999, 1799, 1),
  (2, 2, 1, NULL, 1, 159900, 149900, 2),
  (3, 3, 11, NULL, 1, 4999, 3499, 3),
  (4, 3, 12, NULL, 1, 3999, 2799, 3);

-- Sample data for Wishlist
INSERT INTO wishlist (id, user_id) VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4);

-- Sample data for Wishlist Products
INSERT INTO wishlist_products (wishlist_id, products_id) VALUES
  (1, 1), (1, 3), (1, 9),
  (2, 2), (2, 10), (2, 13),
  (3, 5), (3, 7), (3, 14),
  (4, 4), (4, 11), (4, 15);

-- Sample data for Orders with PaymentDetails
INSERT INTO orders (id, order_id, user_id, seller_id, shipping_address_id, total_mrp_price, total_selling_price, discount, order_status, total_item, payment_status, order_date, deliver_date, payment_id, razorpay_payment_link_id, razorpay_payment_link_reference_id, razorpay_payment_link_status, razorpay_payment_id, status) VALUES
  (1, 'ORD20250101001', 1, 1, 1, 159900, 149900, 10000, 'DELIVERED', 1, 'COMPLETED', '2025-01-01 10:30:00', '2025-01-05 15:00:00', 'PAY123456789', 'link_NrfTDfDFghjk12', 'ref_NrfTDfDFghjk12', 'paid', 'pay_NrfTDfDFghjk12', 'COMPLETED'),
  
  (2, 'ORD20250102002', 2, 2, 3, 18999, 14999, 4000, 'SHIPPED', 1, 'COMPLETED', '2025-01-02 14:20:00', '2025-01-08 18:00:00', 'PAY123456790', 'link_NrfTDfDFghjk13', 'ref_NrfTDfDFghjk13', 'paid', 'pay_NrfTDfDFghjk13', 'COMPLETED'),
  
  (3, 'ORD20250103003', 3, 2, 7, 5598, 3898, 1700, 'PLACED', 2, 'PENDING', '2025-01-03 09:15:00', NULL, NULL, NULL, NULL, NULL, NULL, 'PENDING'),
  
  (4, 'ORD20250104004', 1, 3, 2, 45999, 35999, 10000, 'DELIVERED', 1, 'COMPLETED', '2025-01-04 16:45:00', '2025-01-10 14:30:00', 'PAY123456791', 'link_NrfTDfDFghjk14', 'ref_NrfTDfDFghjk14', 'paid', 'pay_NrfTDfDFghjk14', 'COMPLETED'),
  
  (5, 'ORD20250105005', 4, 1, 8, 49900, 44900, 5000, 'CONFIRMED', 1, 'COMPLETED', '2025-01-05 11:00:00', NULL, 'PAY123456792', 'link_NrfTDfDFghjk15', 'ref_NrfTDfDFghjk15', 'paid', 'pay_NrfTDfDFghjk15', 'COMPLETED');

-- Sample data for Order Items
INSERT INTO order_item (id, order_id, product_id, size, quantity, mrp_price, selling_price, user_id) VALUES
  (1, 1, 1, NULL, 1, 159900, 149900, 1),
  (2, 2, 10, NULL, 1, 18999, 14999, 2),
  (3, 3, 5, 'L', 1, 2999, 1799, 3),
  (4, 3, 6, 'M', 1, 2599, 1599, 3),
  (5, 4, 13, NULL, 1, 45999, 35999, 1),
  (6, 5, 3, NULL, 1, 49900, 44900, 4);

-- Sample data for Payment Orders
INSERT INTO payment_order (id, amount, payment_order_status, payment_method, user_id, payment_link_id) VALUES
  (1, 149900, 'SUCCESS', 'RAZORPAY', 1, 'link_NrfTDfDFghjk12'),
  (2, 14999, 'SUCCESS', 'RAZORPAY', 2, 'link_NrfTDfDFghjk13'),
  (3, 3898, 'PENDING', 'RAZORPAY', 3, NULL),
  (4, 35999, 'SUCCESS', 'RAZORPAY', 1, 'link_NrfTDfDFghjk14'),
  (5, 44900, 'SUCCESS', 'RAZORPAY', 4, 'link_NrfTDfDFghjk15');

-- Link payment orders to orders
INSERT INTO payment_order_orders (payment_order_id, orders_id) VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4),
  (5, 5);

-- Sample data for Transactions
INSERT INTO transaction (id, customer_id, order_id, seller_id, date) VALUES
  (1, 1, 1, 1, '2025-01-01 10:35:00'),
  (2, 2, 2, 2, '2025-01-02 14:25:00'),
  (3, 1, 4, 3, '2025-01-04 16:50:00'),
  (4, 4, 5, 1, '2025-01-05 11:05:00');

-- Sample data for Seller Report
INSERT INTO seller_report (id, seller_id, total_earnings, total_sales, total_refunds, total_tax, net_earnings, total_orders, canceled_orders, total_transactions) VALUES
  (1, 1, 239799, 3, 0, 35969, 203830, 3, 0, 2),
  (2, 2, 18598, 2, 0, 2789, 15809, 2, 0, 1),
  (3, 3, 35999, 1, 0, 5399, 30600, 1, 0, 1);

-- Sample data for Reviews
INSERT INTO review (id, review_text, rating, product_id, user_id, created_at) VALUES
  (1, 'Excellent phone! Camera quality is outstanding and battery life is impressive.', 5, 1, 1, '2025-01-06 10:00:00'),
  (2, 'Beautiful saree with rich fabric. Perfect for special occasions!', 5, 10, 2, '2025-01-09 14:30:00'),
  (3, 'Good quality shirt, fits perfectly. Value for money.', 4, 5, 3, '2025-01-04 09:45:00'),
  (4, 'Comfortable sofa, looks great in my living room. Delivery was smooth.', 4, 13, 1, '2025-01-11 16:20:00'),
  (5, 'Amazing smartwatch! All features work perfectly. Highly recommended.', 5, 3, 4, '2025-01-07 11:15:00'),
  (6, 'Nice design but color slightly differs from the picture.', 3, 6, 3, '2025-01-05 13:00:00'),
  (7, 'Superb quality saree. Received many compliments!', 5, 11, 2, '2025-01-08 15:45:00'),
  (8, 'Good t-shirt for workouts. Material is breathable.', 4, 7, 1, '2025-01-06 08:30:00');

-- Add review images for some reviews
INSERT INTO review_product_images (review_id, product_images) VALUES
  (1, 'assets/images/products/purchased product.jpg'),
  (4, 'assets/images/furniture/Runners 1.jpg'),
  (7, 'assets/images/products/PureChiffonSaree 1.jpg');

-- Sample data for Notifications
INSERT INTO notification (id, customer_id, message, sent_at, read_status) VALUES
  (1, 1, 'Your order ORD20250101001 has been delivered successfully!', '2025-01-05 15:05:00', true),
  (2, 1, 'Welcome to our platform! Use code WELCOME20 for 20% off.', '2025-01-01 09:00:00', true),
  (3, 2, 'Your order ORD20250102002 has been shipped!', '2025-01-03 10:00:00', true),
  (4, 3, 'Flash Sale! Get 30% off on all fashion items. Limited time offer!', '2025-01-03 12:00:00', false),
  (5, 4, 'Your order ORD20250105005 has been confirmed.', '2025-01-05 11:10:00', true),
  (6, 1, 'New furniture collection launched! Check it out now.', '2025-01-06 09:00:00', false);

-- Sample data for Home Categories
INSERT INTO home_category (id, name, image, category_id, section) VALUES
  -- Grid Section
  (1, 'Mobile Phones', 'assets/images/mobiel/-original-imagx9egdetafafz.webp', 'MOBILE_PHONES', 'GRID'),
  (2, 'Fashion', 'assets/images/products/silk-saree.jpg', 'FASHION', 'GRID'),
  (3, 'Electronics', 'assets/images/watch/android-ios-90172ap01-titan-yes-original-imagqggzvzabrhmv 1.webp', 'ELECTRONICS', 'GRID'),
  
  -- Shop By Categories
  (4, 'Men\'s Fashion', 'assets/images/Men shirt/Louis-Philippe-Men-Shirts 1.jpg', 'MEN', 'SHOP_BY_CATEGORIES'),
  (5, 'Women\'s Fashion', 'assets/images/products/banarasi-saree 1.jpg', 'WOMEN', 'SHOP_BY_CATEGORIES'),
  (6, 'Home & Living', 'assets/images/furniture/Runners 1.jpg', 'HOME_FURNITURE', 'SHOP_BY_CATEGORIES'),
  
  -- Electric Categories
  (7, 'Smartphones', 'assets/images/mobiel/-original-imagx9pf7dd5ny7n.webp', 'MOBILE_PHONES', 'ELECTRIC_CATEGORIES'),
  (8, 'Smart Watches', 'assets/images/watch/pro-ray-android-ios-cellecor-yes-original-imagydnsrany7qhy 1.webp', 'SMART_WATCHES', 'ELECTRIC_CATEGORIES'),
  
  -- Deal Categories
  (9, 'Fashion Deals', 'assets/images/products/yellow 1.jpg', 'FASHION', 'DEALS'),
  (10, 'Electronics Deals', 'assets/images/mobiel/-original-imagx9egwjgtgwqf.webp', 'ELECTRONICS', 'DEALS');

-- Sample data for Deals
INSERT INTO deal (id, discount, home_category_id) VALUES
  (1, 20, 9),
  (2, 15, 10),
  (3, 25, 4),
  (4, 30, 5);

-- Sample data for Verification Codes (for testing - normally generated dynamically)
INSERT INTO verification_code (id, otp, email, user_id, seller_id) VALUES
  (1, '123456', 'john.doe@example.com', 1, NULL),
  (2, '234567', 'seller@techworld.com', NULL, 1);

-- Create sample view for better reporting
CREATE OR REPLACE VIEW order_summary AS
SELECT 
    o.id,
    o.order_id,
    u.full_name as customer_name,
    s.seller_name,
    o.total_selling_price,
    o.order_status,
    o.payment_status,
    o.order_date
FROM orders o
JOIN user u ON o.user_id = u.id
JOIN seller s ON o.seller_id = s.id;

-- Update sequences (if using auto-increment)
-- This ensures next inserts will have correct IDs
ALTER TABLE user AUTO_INCREMENT = 7;
ALTER TABLE address AUTO_INCREMENT = 9;
ALTER TABLE categories AUTO_INCREMENT = 17;
ALTER TABLE seller AUTO_INCREMENT = 4;
ALTER TABLE product AUTO_INCREMENT = 16;
ALTER TABLE coupon AUTO_INCREMENT = 6;
ALTER TABLE cart AUTO_INCREMENT = 4;
ALTER TABLE cart_item AUTO_INCREMENT = 5;
ALTER TABLE wishlist AUTO_INCREMENT = 5;
ALTER TABLE orders AUTO_INCREMENT = 6;
ALTER TABLE order_item AUTO_INCREMENT = 7;
ALTER TABLE payment_order AUTO_INCREMENT = 6;
ALTER TABLE transaction AUTO_INCREMENT = 5;
ALTER TABLE seller_report AUTO_INCREMENT = 4;
ALTER TABLE review AUTO_INCREMENT = 9;
ALTER TABLE notification AUTO_INCREMENT = 7;
ALTER TABLE home_category AUTO_INCREMENT = 11;
ALTER TABLE deal AUTO_INCREMENT = 5;
ALTER TABLE verification_code AUTO_INCREMENT = 3;

-- Display summary
SELECT 'Data insertion completed successfully!' as Status;
SELECT 
    (SELECT COUNT(*) FROM user) as Users,
    (SELECT COUNT(*) FROM seller) as Sellers,
    (SELECT COUNT(*) FROM product) as Products,
    (SELECT COUNT(*) FROM orders) as Orders,
    (SELECT COUNT(*) FROM review) as Reviews;
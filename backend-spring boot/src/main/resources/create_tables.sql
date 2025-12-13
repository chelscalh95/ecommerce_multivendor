-- Create database schema manually for ecommercemultivendor
USE ecommercemultivendor;

-- Drop foreign keys and tables if they exist
SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables in reverse order of dependencies
DROP TABLE IF EXISTS review_product_images;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS payment_order_orders;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS payment_order;
DROP TABLE IF EXISTS wishlist_products;
DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS user_coupons;
DROP TABLE IF EXISTS coupon;
DROP TABLE IF EXISTS product_images;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS seller_report;
DROP TABLE IF EXISTS seller;
DROP TABLE IF EXISTS deal;
DROP TABLE IF EXISTS home_category;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS password_reset_token;
DROP TABLE IF EXISTS verification_code;
DROP TABLE IF EXISTS user_addresses;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS user;

SET FOREIGN_KEY_CHECKS = 1;

-- Create User table
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    mobile VARCHAR(20),
    role ENUM('CUSTOMER', 'SELLER', 'ADMIN') DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Address table
CREATE TABLE address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    locality VARCHAR(255),
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    pin_code VARCHAR(10),
    mobile VARCHAR(20)
);

-- Create User-Address relationship table
CREATE TABLE user_addresses (
    user_id BIGINT,
    addresses_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (addresses_id) REFERENCES address(id)
);

-- Create Categories table
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category_id VARCHAR(100) UNIQUE,
    parent_category_id BIGINT,
    level INT DEFAULT 1,
    FOREIGN KEY (parent_category_id) REFERENCES categories(id)
);

-- Create Seller table
CREATE TABLE seller (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_name VARCHAR(255) NOT NULL,
    mobile VARCHAR(20),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    gstin VARCHAR(50),
    is_email_verified BOOLEAN DEFAULT FALSE,
    account_status ENUM('PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DEACTIVATED') DEFAULT 'PENDING_VERIFICATION',
    business_name VARCHAR(255),
    business_email VARCHAR(255),
    business_mobile VARCHAR(20),
    business_address TEXT,
    logo VARCHAR(500),
    banner VARCHAR(500),
    account_number VARCHAR(50),
    account_holder_name VARCHAR(255),
    ifsc_code VARCHAR(20),
    pickup_address_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pickup_address_id) REFERENCES address(id)
);

-- Create Product table
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    mrp_price DECIMAL(10,2),
    selling_price DECIMAL(10,2),
    discount_percent INT,
    quantity INT DEFAULT 0,
    color VARCHAR(100),
    sizes VARCHAR(500),
    num_ratings INT DEFAULT 0,
    category_id BIGINT,
    seller_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    in_stock BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (seller_id) REFERENCES seller(id)
);

-- Create Product Images table
CREATE TABLE product_images (
    product_id BIGINT,
    images VARCHAR(500),
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Create Coupon table
CREATE TABLE coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    discount_percentage DECIMAL(5,2),
    validity_start_date DATE,
    validity_end_date DATE,
    minimum_order_value DECIMAL(10,2),
    is_active BOOLEAN DEFAULT TRUE
);

-- Create User-Coupon relationship table
CREATE TABLE user_coupons (
    user_id BIGINT,
    coupons_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (coupons_id) REFERENCES coupon(id)
);

-- Create Cart table
CREATE TABLE cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE,
    total_selling_price DECIMAL(10,2) DEFAULT 0,
    total_item INT DEFAULT 0,
    total_mrp_price DECIMAL(10,2) DEFAULT 0,
    discount DECIMAL(10,2) DEFAULT 0,
    coupon_code VARCHAR(50),
    coupon_price DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- Create Cart Item table
CREATE TABLE cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT,
    product_id BIGINT,
    size VARCHAR(50),
    quantity INT DEFAULT 1,
    mrp_price DECIMAL(10,2),
    selling_price DECIMAL(10,2),
    user_id BIGINT,
    FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Create Wishlist table
CREATE TABLE wishlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- Create Wishlist-Product relationship table
CREATE TABLE wishlist_products (
    wishlist_id BIGINT,
    products_id BIGINT,
    FOREIGN KEY (wishlist_id) REFERENCES wishlist(id) ON DELETE CASCADE,
    FOREIGN KEY (products_id) REFERENCES product(id)
);

-- Create Payment Order table
CREATE TABLE payment_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10,2),
    payment_order_status ENUM('PENDING', 'SUCCESS', 'FAILED') DEFAULT 'PENDING',
    payment_method ENUM('RAZORPAY', 'STRIPE', 'CASH_ON_DELIVERY') DEFAULT 'RAZORPAY',
    user_id BIGINT,
    payment_link_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Create Orders table
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(100) UNIQUE,
    user_id BIGINT,
    seller_id BIGINT,
    shipping_address_id BIGINT,
    total_mrp_price DECIMAL(10,2),
    total_selling_price DECIMAL(10,2),
    discount DECIMAL(10,2),
    order_status ENUM('PLACED', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PLACED',
    total_item INT,
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deliver_date TIMESTAMP NULL,
    payment_id VARCHAR(255),
    razorpay_payment_link_id VARCHAR(255),
    razorpay_payment_link_reference_id VARCHAR(255),
    razorpay_payment_link_status VARCHAR(100),
    razorpay_payment_id VARCHAR(255),
    status ENUM('PENDING', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (seller_id) REFERENCES seller(id),
    FOREIGN KEY (shipping_address_id) REFERENCES address(id)
);

-- Create Payment Order-Orders relationship table
CREATE TABLE payment_order_orders (
    payment_order_id BIGINT,
    orders_id BIGINT,
    FOREIGN KEY (payment_order_id) REFERENCES payment_order(id),
    FOREIGN KEY (orders_id) REFERENCES orders(id)
);

-- Create Order Item table
CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    size VARCHAR(50),
    quantity INT,
    mrp_price DECIMAL(10,2),
    selling_price DECIMAL(10,2),
    user_id BIGINT,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Create Transaction table
CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT,
    order_id BIGINT,
    seller_id BIGINT,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES user(id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (seller_id) REFERENCES seller(id)
);

-- Create Seller Report table
CREATE TABLE seller_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT UNIQUE,
    total_earnings DECIMAL(15,2) DEFAULT 0,
    total_sales BIGINT DEFAULT 0,
    total_refunds DECIMAL(15,2) DEFAULT 0,
    total_tax DECIMAL(15,2) DEFAULT 0,
    net_earnings DECIMAL(15,2) DEFAULT 0,
    total_orders BIGINT DEFAULT 0,
    canceled_orders BIGINT DEFAULT 0,
    total_transactions BIGINT DEFAULT 0,
    FOREIGN KEY (seller_id) REFERENCES seller(id) ON DELETE CASCADE
);

-- Create Review table
CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_text TEXT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    product_id BIGINT,
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Create Review Product Images table
CREATE TABLE review_product_images (
    review_id BIGINT,
    product_images VARCHAR(500),
    FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE
);

-- Create Notification table
CREATE TABLE notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT,
    message TEXT,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_status BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (customer_id) REFERENCES user(id)
);

-- Create Home Category table
CREATE TABLE home_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    image VARCHAR(500),
    category_id VARCHAR(100),
    section ENUM('GRID', 'SHOP_BY_CATEGORIES', 'ELECTRIC_CATEGORIES', 'DEALS') DEFAULT 'GRID'
);

-- Create Deal table
CREATE TABLE deal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    discount INT,
    home_category_id BIGINT UNIQUE,
    FOREIGN KEY (home_category_id) REFERENCES home_category(id) ON DELETE CASCADE
);

-- Create Verification Code table
CREATE TABLE verification_code (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    otp VARCHAR(10),
    email VARCHAR(255),
    user_id BIGINT,
    seller_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (seller_id) REFERENCES seller(id)
);

-- Create Password Reset Token table
CREATE TABLE password_reset_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255),
    user_id BIGINT,
    expiry_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

SELECT 'Database schema created successfully!' as Status;
SHOW TABLES;
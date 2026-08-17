USE ecommerce_multi_vendor;

START TRANSACTION;

INSERT INTO categories (
    id,
    category_id,
    level,
    name,
    parent_category_id
)
VALUES (
    990001,
    'electronics',
    1,
    'Electronics',
    NULL
)
ON DUPLICATE KEY UPDATE
    category_id = VALUES(category_id),
    level = VALUES(level),
    name = VALUES(name),
    parent_category_id = VALUES(parent_category_id);

INSERT INTO categories (
    id,
    category_id,
    level,
    name,
    parent_category_id
)
VALUES (
    990002,
    'mobiles',
    2,
    'Mobile Phones',
    990001
)
ON DUPLICATE KEY UPDATE
    category_id = VALUES(category_id),
    level = VALUES(level),
    name = VALUES(name),
    parent_category_id = VALUES(parent_category_id);

INSERT INTO seller (
    id,
    email,
    is_email_verified,
    seller_name,
    business_name,
    business_email
)
VALUES (
    990001,
    'catalog.seller@ecommerce.test',
    1,
    'CI Catalog Seller',
    'CI Catalog Store',
    'catalog.store@ecommerce.test'
)
ON DUPLICATE KEY UPDATE
    email = VALUES(email),
    is_email_verified = VALUES(is_email_verified),
    seller_name = VALUES(seller_name),
    business_name = VALUES(business_name),
    business_email = VALUES(business_email);

INSERT INTO product (
    id,
    title,
    description,
    mrp_price,
    selling_price,
    discount_percent,
    quantity,
    color,
    num_ratings,
    category_id,
    seller_id,
    created_at,
    sizes,
    in_stock
)
VALUES
(
    990001,
    'CI Test Smartphone',
    'Deterministic smartphone used by API tests',
    100000,
    90000,
    10,
    25,
    'Black',
    0,
    990002,
    990001,
    CURRENT_TIMESTAMP(6),
    NULL,
    1
),
(
    990002,
    'CI Test Mobile',
    'Second deterministic mobile product',
    80000,
    70000,
    12,
    15,
    'Blue',
    0,
    990002,
    990001,
    CURRENT_TIMESTAMP(6),
    NULL,
    1
)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    description = VALUES(description),
    mrp_price = VALUES(mrp_price),
    selling_price = VALUES(selling_price),
    discount_percent = VALUES(discount_percent),
    quantity = VALUES(quantity),
    color = VALUES(color),
    num_ratings = VALUES(num_ratings),
    category_id = VALUES(category_id),
    seller_id = VALUES(seller_id),
    sizes = VALUES(sizes),
    in_stock = VALUES(in_stock);

DELETE FROM product_images
WHERE product_id IN (990001, 990002);

INSERT INTO product_images (product_id, images)
VALUES
    (990001, 'https://images.example.test/ci-smartphone.jpg'),
    (990002, 'https://images.example.test/ci-mobile.jpg');

COMMIT;
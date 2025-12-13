-- Use ecommercemultivendor database
USE ecommercemultivendor;

-- Sample data for User (Customers) - Essential table
INSERT IGNORE INTO user (id, password, email, full_name, mobile, role) VALUES
  (1, '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', 'john.doe@example.com', 'John Doe', '9876543210', 'CUSTOMER'),
  (2, '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', 'jane.smith@example.com', 'Jane Smith', '9876543211', 'CUSTOMER'),
  (3, '$2a$10$Jp5KuPiOAJvLw5xrFCKD1OWEaxsNO3I3mzHJ.ZTVqEq5A8BjI6Mu.', 'admin@example.com', 'System Admin', '9876543215', 'ADMIN');

-- Sample data for Address - Essential table
INSERT IGNORE INTO address (id, name, locality, address, city, state, pin_code, mobile) VALUES
  (1, 'John Home', 'Indiranagar', '123 Main Street, Apt 4B', 'Bangalore', 'Karnataka', '560038', '9876543210'),
  (2, 'Jane Home', 'Bandra', '789 Sea View Apartments', 'Mumbai', 'Maharashtra', '400050', '9876543211'),
  (3, 'Electronics Hub', 'Nehru Place', 'Electronics Market, Block B', 'New Delhi', 'Delhi', '110019', '9876543222');

-- Sample data for Categories - Essential table
INSERT IGNORE INTO categories (id, name, category_id, parent_category_id, level) VALUES
  (1, 'Electronics', 'ELECTRONICS', NULL, 1),
  (2, 'Fashion', 'FASHION', NULL, 1),
  (3, 'Home & Furniture', 'HOME_FURNITURE', NULL, 1),
  (5, 'Mobile Phones', 'MOBILE_PHONES', 1, 2),
  (7, 'Smart Watches', 'SMART_WATCHES', 1, 2),
  (8, 'Men', 'MEN', 2, 2),
  (9, 'Women', 'WOMEN', 2, 2),
  (11, 'Men Shirts', 'MEN_SHIRTS', 8, 3),
  (13, 'Sarees', 'SAREES', 9, 3);

-- Display summary
SELECT 'Essential data insertion completed!' as Status;
SELECT 
    (SELECT COUNT(*) FROM user) as Users,
    (SELECT COUNT(*) FROM address) as Addresses,
    (SELECT COUNT(*) FROM categories) as Categories;
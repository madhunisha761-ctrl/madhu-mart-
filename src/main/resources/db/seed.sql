INSERT INTO users (name, email, password_hash, role) VALUES
('Admin', 'admin@madhumart.com', '$2a$10$6m723.eZX533lkbfpbD23Om2BWyY5N3BKfgjcrAsqonJ475oldI.S', 'ADMIN'),
('Ravi Seller', 'ravi@madhumart.com', '$2a$10$6m723.eZX533lkbfpbD23Om2BWyY5N3BKfgjcrAsqonJ475oldI.S', 'SELLER'),
('Priya Seller', 'priya@madhumart.com', '$2a$10$6m723.eZX533lkbfpbD23Om2BWyY5N3BKfgjcrAsqonJ475oldI.S', 'SELLER'),
('Madhu Buyer', 'buyer@madhumart.com', '$2a$10$6m723.eZX533lkbfpbD23Om2BWyY5N3BKfgjcrAsqonJ475oldI.S', 'BUYER');

INSERT INTO products (seller_id, name, description, category, price, stock) VALUES
(2, 'Wireless Earbuds', 'Bluetooth 5.0 earbuds with charging case', 'Electronics', 1499.00, 25),
(2, 'Power Bank 10000mAh', 'Fast charging power bank', 'Electronics', 999.00, 40),
(2, 'Smart Watch', 'Fitness tracker with heart rate monitor', 'Electronics', 2499.00, 15),
(3, 'Cotton T-Shirt', 'Comfortable everyday cotton t-shirt', 'Fashion', 399.00, 60),
(3, 'Denim Jeans', 'Slim fit blue denim jeans', 'Fashion', 1199.00, 30),
(3, 'Java Programming Book', 'Beginner friendly Java guide', 'Books', 549.00, 20),
(3, 'Steel Water Bottle', '1 litre insulated water bottle', 'Home', 349.00, 50),
(2, 'LED Desk Lamp', 'Adjustable brightness desk lamp', 'Home', 699.00, 35);

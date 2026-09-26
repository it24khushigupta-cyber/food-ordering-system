INSERT INTO users (name, email, password, role) VALUES
('Demo Customer', 'demo@foodapp.com', 'password123', 'CUSTOMER'),
('Admin User', 'admin@foodapp.com', 'admin123', 'ADMIN');

INSERT INTO food_items (name, description, price, category, available) VALUES
('Margherita Pizza', 'Classic pizza with tomato, mozzarella and basil', 249.00, 'Pizza', true),
('Farmhouse Pizza', 'Loaded with onion, capsicum, tomato and mushroom', 299.00, 'Pizza', true),
('Veg Burger', 'Crispy veg patty with lettuce and mayo', 99.00, 'Burgers', true),
('Chicken Burger', 'Grilled chicken patty with cheese', 149.00, 'Burgers', true),
('Paneer Tikka', 'Char-grilled cottage cheese with spices', 199.00, 'Starters', true),
('French Fries', 'Crispy salted fries', 89.00, 'Sides', true),
('Cold Coffee', 'Chilled coffee with ice cream', 79.00, 'Beverages', true),
('Chocolate Brownie', 'Warm brownie with chocolate sauce', 119.00, 'Desserts', true);

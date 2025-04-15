
INSERT INTO users (username, email, password, role, rating, balance) VALUES
                                                                         ('admin1', 'admin1@example.com', 'password123', 'admin', 4.5, 100.00),
                                                                         ('buyer1', 'buyer1@example.com', 'password123', 'buyer', 4.2, 50.00),
                                                                         ('seller1', 'seller1@example.com', 'password123', 'seller', 4.0, 200.00),
                                                                         ('buyer2', 'buyer2@example.com', 'password123', 'buyer', 4.8, 30.00),
                                                                         ('seller2', 'seller2@example.com', 'password123', 'seller', 3.9, 500.00),
                                                                         ('admin2', 'admin2@example.com', 'password123', 'admin', 4.7, 200.00),
                                                                         ('guest1', 'guest1@example.com', 'password123', 'guest', 0.0, 0.00),
                                                                         ('buyer3', 'buyer3@example.com', 'password123', 'buyer', 4.3, 70.00),
                                                                         ('seller3', 'seller3@example.com', 'password123', 'seller', 3.5, 150.00),
                                                                         ('guest2', 'guest2@example.com', 'password123', 'guest', 0.0, 0.00);


INSERT INTO products (seller_id, name, description, price, stock) VALUES
                                                                      (3, 'Product 1', 'Description of Product 1', 100.00, 50),
                                                                      (3, 'Product 2', 'Description of Product 2', 150.00, 30),
                                                                      (4, 'Product 3', 'Description of Product 3', 200.00, 20),
                                                                      (5, 'Product 4', 'Description of Product 4', 80.00, 100),
                                                                      (6, 'Product 5', 'Description of Product 5', 120.00, 60),
                                                                      (7, 'Product 6', 'Description of Product 6', 250.00, 10),
                                                                      (8, 'Product 7', 'Description of Product 7', 90.00, 40),
                                                                      (9, 'Product 8', 'Description of Product 8', 300.00, 5),
                                                                      (10, 'Product 9', 'Description of Product 9', 70.00, 200),
                                                                      (10, 'Product 10', 'Description of Product 10', 50.00, 300);


INSERT INTO orders (buyer_id, seller_id, product_id, quantity, total_price, status) VALUES
                                                                                        (2, 3, 1, 2, 200.00, 'pending'),
                                                                                        (4, 5, 3, 1, 200.00, 'shipped'),
                                                                                        (6, 7, 6, 1, 250.00, 'delivered'),
                                                                                        (3, 4, 2, 3, 450.00, 'canceled'),
                                                                                        (5, 6, 5, 5, 600.00, 'pending'),
                                                                                        (7, 8, 7, 2, 180.00, 'shipped'),
                                                                                        (8, 9, 8, 1, 300.00, 'delivered'),
                                                                                        (9, 10, 9, 2, 140.00, 'pending'),
                                                                                        (10, 2, 10, 10, 500.00, 'shipped'),
                                                                                        (1, 5, 4, 4, 320.00, 'delivered');


INSERT INTO auctions (product_id, seller_id, start_price, current_price, start_time, end_time) VALUES
                                                                                                   (1, 3, 100.00, 150.00, '2025-04-01 10:00:00', '2025-04-10 10:00:00'),
                                                                                                   (2, 4, 150.00, 200.00, '2025-04-02 10:00:00', '2025-04-12 10:00:00'),
                                                                                                   (3, 5, 80.00, 120.00, '2025-04-03 10:00:00', '2025-04-15 10:00:00'),
                                                                                                   (4, 6, 200.00, 250.00, '2025-04-04 10:00:00', '2025-04-20 10:00:00'),
                                                                                                   (5, 7, 90.00, 140.00, '2025-04-05 10:00:00', '2025-04-18 10:00:00'),
                                                                                                   (6, 8, 250.00, 300.00, '2025-04-06 10:00:00', '2025-04-22 10:00:00'),
                                                                                                   (7, 9, 70.00, 110.00, '2025-04-07 10:00:00', '2025-04-25 10:00:00'),
                                                                                                   (8, 10, 120.00, 170.00, '2025-04-08 10:00:00', '2025-04-28 10:00:00'),
                                                                                                   (9, 2, 250.00, 350.00, '2025-04-09 10:00:00', '2025-04-30 10:00:00'),
                                                                                                   (10, 3, 50.00, 80.00, '2025-04-10 10:00:00', '2025-05-05 10:00:00');


INSERT INTO bids (auction_id, user_id, bid_amount) VALUES
                                                       (1, 2, 120.00),
                                                       (1, 3, 130.00),
                                                       (2, 4, 180.00),
                                                       (2, 5, 190.00),
                                                       (3, 6, 90.00),
                                                       (4, 7, 220.00),
                                                       (5, 8, 100.00),
                                                       (6, 9, 280.00),
                                                       (7, 10, 80.00),
                                                       (8, 2, 160.00);


INSERT INTO seller_ratings (seller_id, buyer_id, rating, review) VALUES
                                                                     (3, 2, 4.5, 'Great product!'),
                                                                     (4, 5, 3.8, 'The product was okay, but shipping was delayed.'),
                                                                     (6, 8, 5.0, 'Perfect transaction, highly recommend!'),
                                                                     (7, 9, 4.2, 'Good quality, but a bit pricey.'),
                                                                     (8, 10, 4.7, 'Fast shipping and good customer service.'),
                                                                     (9, 1, 4.0, 'Product was good, but expected better quality.'),
                                                                     (3, 7, 4.8, 'Excellent experience!'),
                                                                     (5, 2, 3.9, 'The product did not meet my expectations.'),
                                                                     (10, 6, 4.6, 'Very happy with my purchase.'),
                                                                     (4, 3, 4.1, 'Good value for the price.');


INSERT INTO achievements (user_id, achievement_name, points) VALUES
                                                                 (1, 'First Sale', 100),
                                                                 (2, 'Top Bidder', 200),
                                                                 (3, '100 Products Sold', 500),
                                                                 (4, 'Auction Winner', 300),
                                                                 (5, 'Five Star Rating', 150),
                                                                 (6, 'Best Seller', 400),
                                                                 (7, 'First Purchase', 50),
                                                                 (8, 'High Roller', 350),
                                                                 (9, 'Product Expert', 250),
                                                                 (10, 'Super Buyer', 600);

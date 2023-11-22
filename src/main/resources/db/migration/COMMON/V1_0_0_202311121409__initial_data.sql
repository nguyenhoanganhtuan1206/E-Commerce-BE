-- Insert roles if they do not exist
INSERT INTO roles (id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_SELLER');

-- Seed Data for User
INSERT INTO users (id, code_reset_password, created_at, email, last_send_reset_password_at, password,
                   phone_number, selling_enabled, updated_at, username)
VALUES ('d54e9d14-fd3c-4936-8d7f-66b8f48e8661', NULL, NOW(), 'admin@gmail.com', NULL,
        '$2a$12$P3EiXFODL6rumybPKjn.Nui.y7H/yGNlcwI3uI.YdUkWIkJtdMX/K', NULL, FALSE, NULL, 'Admin'),
       ('237c736b-9aa3-4c4e-8d7e-558166bb3d6b', NULL, NOW(),
        'nguyenhoanganhtuan1206@gmail.com', NULL, '$2a$10$8LZTNRd9SbEz8ra53nQGtOYrNntm1AA75jv294mxuxyj6/q6jLSi2',
        '0903703541', FALSE, NULL, 'Anh Tuan'),
       ('9cb2ed6a-1753-4232-9c5c-8c523e32546e', NULL, NOW(),
        'nguyenhanhtuan1206@gmail.com', NULL, '$2a$10$8LZTNRd9SbEz8ra53nQGtOYrNntm1AA75jv294mxuxyj6/q6jLSi2',
        '0903703541', FALSE, NULL, 'Anh Tuan 1'),
       ('054cbe26-67b7-4ead-b4f7-e72687d5f3ac', NULL, NOW(),
        'tuannguyen@gmail.com', NULL, '$2a$10$8LZTNRd9SbEz8ra53nQGtOYrNntm1AA75jv294mxuxyj6/q6jLSi2',
        '0903703541', FALSE, NULL, 'Anh Tuan 2');

INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE email = 'admin@gmail.com'), 1),
       ('9cb2ed6a-1753-4232-9c5c-8c523e32546e', 2),
       ('237c736b-9aa3-4c4e-8d7e-558166bb3d6b', 2),
       ('237c736b-9aa3-4c4e-8d7e-558166bb3d6b', 3),
       ('054cbe26-67b7-4ead-b4f7-e72687d5f3ac', 2),
       ('054cbe26-67b7-4ead-b4f7-e72687d5f3ac', 3);

-- Insert payment methods if they do not exist
INSERT INTO payment_method (name)
VALUES ('COD'),
       ('Paypal');

INSERT INTO category (category_name)
VALUES ('Sportswear'),
       ('Men'),
       ('Women'),
       ('Kids'),
       ('Accessories');

-- Seed Data for Brand
INSERT INTO brand (brand_name)
VALUES ('Dior'),
       ('Gucci'),
       ('Nike'),
       ('Adidas'),
       ('Coolmate'),
       ('Anna');

-- Seed Data for product_variant
INSERT INTO product_variant (name)
VALUES ('Shirt'),
       ('Trouser'),
       ('Dress'),
       ('Shoes'),
       ('Bags'),
       ('Jean');

-- Seed Data for Styles
INSERT INTO product_style (name)
VALUES ('Plain'),
       ('Pattern'),
       ('Polka Dots'),
       ('Plaid');


-- Seed Data for Seller
INSERT INTO sellers (id, address, commune, created_at, district, email_seller, phone_number, province,
                     seller_approval, seller_name, seller_rating, updated_at, user_id)
VALUES ('759d3c5e-175d-48dd-a0ce-06a2bc20d695', 'K135/14 Nguyễn Du', 'Phường Thuận Phước', '2023-11-16 22:03:23.824898',
        'Quận Hải Châu', 'nguyenhanhtuan1206@gmail.com', '090703541', 'Thành phố Đà Nẵng', 'ACTIVE', 'Shopee', 0, NULL,
        '237c736b-9aa3-4c4e-8d7e-558166bb3d6b'),
       ('3fc327da-9fbb-4e65-b801-64ab8135c372', 'K135/14 Nguyễn Du', 'Phường Thuận Phước', '2023-11-16 22:03:23.824898',
        'Quận Hải Châu', 'nguyenhoanganhtuan1206@gmail.com', '090703541', 'Thành phố Đà Nẵng', 'ACTIVE', 'Shopee 1', 0,
        NULL,
        '054cbe26-67b7-4ead-b4f7-e72687d5f3ac');

-- Seed Data for Product
INSERT INTO products (id, amount_sold_out, created_at, description, name, price, product_approval, quantity,
                      updated_at, brand_id, product_variant_id, seller_id)
VALUES ('bc6787fd-2e70-4629-af97-ff44ccd35ccb', NULL, NOW(),
        'This is a sample product description.', 'Sample Product', 99, 'ACTIVE', 0, NOW(),
        (SELECT id FROM brand WHERE brand_name = 'Gucci'),
        (SELECT id FROM product_variant WHERE product_variant.name = 'Shirt'),
        '759d3c5e-175d-48dd-a0ce-06a2bc20d695'),
       ('fbe25bc9-09d8-463e-a7c6-4ce69d04e8d2', NULL, NOW(),
        'This is a sample product description.', 'Sample Product 1', 99, 'ACTIVE', 50, NOW(),
        (SELECT id FROM brand WHERE brand_name = 'Gucci'),
        (SELECT id FROM product_variant WHERE product_variant.name = 'Shirt'),
        '759d3c5e-175d-48dd-a0ce-06a2bc20d695'),
       ('1b87fff1-a469-4220-8968-cfccad5c55e5', NULL, NOW(),
        'This is a sample product description.', 'Sample Product 2', 99, 'ACTIVE', 0, NOW(),
        (SELECT id FROM brand WHERE brand_name = 'Gucci'),
        (SELECT id FROM product_variant WHERE product_variant.name = 'Shirt'),
        '3fc327da-9fbb-4e65-b801-64ab8135c372');

INSERT INTO inventories (id, color_name, color_value, price, quantity, size_name, size_value, product_id)
VALUES ('2dc1f7c9-0439-4071-9f80-9e94721ac35f', 'Color', 'Red', 10, 10, 'Size', 'L',
        'bc6787fd-2e70-4629-af97-ff44ccd35ccb'),
       ('56066fe4-9b40-47c5-a260-e3f81cd04701', 'Color', 'Blue', 10, 10, 'Size', 'XL',
        '1b87fff1-a469-4220-8968-cfccad5c55e5');

-- Seed Data for Product Style
INSERT INTO product_product_style
VALUES ('fbe25bc9-09d8-463e-a7c6-4ce69d04e8d2', (SELECT id FROM product_style WHERE name = 'Plain')),
       ('bc6787fd-2e70-4629-af97-ff44ccd35ccb', (SELECT id FROM product_style WHERE name = 'Pattern')),
       ('1b87fff1-a469-4220-8968-cfccad5c55e5', (SELECT id FROM product_style WHERE name = 'Plain'));

-- Seed Data for Cart
INSERT INTO cart(id, total_price, is_payment, created_at, user_id, seller_id)
VALUES ('054cbe26-67b7-4ead-b4f7-e72687d5f3ac', 100, FALSE, NOW(), '9cb2ed6a-1753-4232-9c5c-8c523e32546e',
        '759d3c5e-175d-48dd-a0ce-06a2bc20d695'),
       ('20ee2988-b9c3-4c1c-b5e0-dad5f1a57808', 100, FALSE, NOW(), '054cbe26-67b7-4ead-b4f7-e72687d5f3ac',
        '759d3c5e-175d-48dd-a0ce-06a2bc20d695');

-- Seed Data for Cart Product Inventory
INSERT INTO cart_product_inventory(quantity, cart_id, inventory_id, product_id)
VALUES (5, '054cbe26-67b7-4ead-b4f7-e72687d5f3ac', '2dc1f7c9-0439-4071-9f80-9e94721ac35f', NULL),
       (5, '054cbe26-67b7-4ead-b4f7-e72687d5f3ac', '56066fe4-9b40-47c5-a260-e3f81cd04701', NULL);


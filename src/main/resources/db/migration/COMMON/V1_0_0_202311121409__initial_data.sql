-- Insert roles if they do not exist
INSERT INTO roles (id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_SELLER');

-- Seed Data for User
INSERT INTO users (id, address, code_reset_password, created_at, email, last_send_reset_password_at, password,
                   phone_number, selling_enabled, updated_at, username)
VALUES ('d54e9d14-fd3c-4936-8d7f-66b8f48e8661', NULL, NULL, NOW(), 'admin@gmail.com', NULL,
        '$2a$12$P3EiXFODL6rumybPKjn.Nui.y7H/yGNlcwI3uI.YdUkWIkJtdMX/K', NULL, FALSE, NULL, 'Admin'),
       ('237c736b-9aa3-4c4e-8d7e-558166bb3d6b', 'K135/14 Nguyễn Du', NULL, NOW(),
        'nguyenhoanganhtuan1206@gmail.com', NULL, '$2a$10$8LZTNRd9SbEz8ra53nQGtOYrNntm1AA75jv294mxuxyj6/q6jLSi2',
        '0903703541', FALSE, NULL, 'Anh Tuan');

INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE email = 'admin@gmail.com'), 1);

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
        '237c736b-9aa3-4c4e-8d7e-558166bb3d6b');

-- Seed Data for Product
INSERT INTO products (id, amount_sold_out, created_at, description, name, price, product_approval, quantity,
                      updated_at, brand_id, product_variant_id, seller_id)
VALUES ('bc6787fd-2e70-4629-af97-ff44ccd35ccb', NULL, NOW(),
        'This is a sample product description.', 'Sample Product', 99, 'ACTIVE', 50, NOW(),
        (SELECT id FROM brand WHERE brand_name = 'Gucci' LIMIT 1),
        (SELECT id FROM product_variant WHERE product_variant.name = 'Shirt' LIMIT 1),
        '759d3c5e-175d-48dd-a0ce-06a2bc20d695');

INSERT INTO inventories (id, color_name, color_value, price, quantity, size_name, size_value, product_id)
VALUES ('2dc1f7c9-0439-4071-9f80-9e94721ac35f', 'Color', 'Red', 5, 10, 'Size', 'L',
        'bc6787fd-2e70-4629-af97-ff44ccd35ccb');
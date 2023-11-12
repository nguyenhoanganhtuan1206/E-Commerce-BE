-- Insert roles if they do not exist
INSERT INTO roles (id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_SELLER');

-- Insert admin user if it does not exist
INSERT INTO users (email, username, password, created_at)
VALUES ('admin@gmail.com', 'Admin', '$2a$12$P3EiXFODL6rumybPKjn.Nui.y7H/yGNlcwI3uI.YdUkWIkJtdMX/K', NOW());

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
       ('Accessories'),
       ('Sportswear');

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

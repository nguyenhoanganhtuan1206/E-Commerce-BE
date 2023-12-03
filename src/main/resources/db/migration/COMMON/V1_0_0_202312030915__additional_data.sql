-- Seed Data for Product and Category
INSERT INTO product_category (product_id, category_id)
VALUES ('bc6787fd-2e70-4629-af97-ff44ccd35ccb', (SELECT id FROM category WHERE category_name = 'Sportswear')),
       ('fbe25bc9-09d8-463e-a7c6-4ce69d04e8d2', (SELECT id FROM category WHERE category_name = 'Men')),
       ('1b87fff1-a469-4220-8968-cfccad5c55e5', (SELECT id FROM category WHERE category_name = 'Women'));

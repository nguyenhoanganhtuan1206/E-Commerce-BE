CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users
(
    id                          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    address                     VARCHAR(255)     DEFAULT NULL,
    code_reset_password         VARCHAR(255)     DEFAULT NULL,
    created_at                  TIMESTAMP        DEFAULT NOW(),
    email                       VARCHAR(255)     DEFAULT NULL,
    last_send_reset_password_at TIMESTAMP        DEFAULT NULL,
    password                    VARCHAR(1024)    DEFAULT NULL,
    phone_number                VARCHAR(255)     DEFAULT NULL,
    selling_enabled             BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at                  TIMESTAMP        DEFAULT NULL,
    username                    VARCHAR(255)     DEFAULT NULL
);

CREATE TABLE roles
(
    id   INT PRIMARY KEY,
    name VARCHAR(255) DEFAULT NULL
);

CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    role_id INT  NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_role_user FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE payment_method
(
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255)     DEFAULT NULL
);

CREATE TABLE category
(
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    category_name VARCHAR(255)     DEFAULT NULL
);

CREATE TABLE brand
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    brand_name VARCHAR(255)     DEFAULT NULL
);

CREATE TABLE product_variant
(
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255)     DEFAULT NULL
);

CREATE TABLE sellers
(
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    address         VARCHAR(255)     DEFAULT NULL,
    commune         VARCHAR(255)     DEFAULT NULL,
    created_at      TIMESTAMP        DEFAULT NOW(),
    district        VARCHAR(255)     DEFAULT NULL,
    email_seller    VARCHAR(255)     DEFAULT NULL,
    phone_number    VARCHAR(255)     DEFAULT NULL,
    province        VARCHAR(255)     DEFAULT NULL,
    seller_approval VARCHAR(255)     DEFAULT NULL,
    seller_name     VARCHAR(255)     DEFAULT NULL,
    seller_rating   FLOAT NOT NULL,
    updated_at      TIMESTAMP        DEFAULT NULL,
    user_id         UUID             DEFAULT NULL,
    CONSTRAINT fk_sellers_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE product_style
(
    id        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name      VARCHAR(255)     DEFAULT NULL,
    seller_id UUID             DEFAULT NULL,
    CONSTRAINT fk_seller_product_style FOREIGN KEY (seller_id) REFERENCES sellers (id)
);

CREATE TABLE products
(
    id                 UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    amount_sold_out    BIGINT           DEFAULT NULL,
    created_at         TIMESTAMP        DEFAULT NOW(),
    description        TEXT             DEFAULT NULL,
    name               VARCHAR(255)     DEFAULT NULL,
    price              BIGINT NOT NULL,
    product_approval   VARCHAR(255)     DEFAULT NULL,
    quantity           BIGINT NOT NULL,
    updated_at         TIMESTAMP        DEFAULT NOW(),
    brand_id           UUID   NOT NULL,
    product_variant_id UUID   NOT NULL,
    seller_id          UUID   NOT NULL,
    CONSTRAINT fk_seller_product FOREIGN KEY (seller_id) REFERENCES sellers (id),
    CONSTRAINT fk_product_variant_product FOREIGN KEY (product_variant_id) REFERENCES product_variant (id),
    CONSTRAINT fk_brand_product FOREIGN KEY (brand_id) REFERENCES brand (id)
);

CREATE TABLE product_product_style
(
    product_id       UUID NOT NULL,
    product_style_id UUID NOT NULL,
    PRIMARY KEY (product_id, product_style_id),
    CONSTRAINT fk_product_product_style FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_product_style FOREIGN KEY (product_style_id) REFERENCES product_style (id)
);

CREATE TABLE inventories
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    color_name  VARCHAR(255)     DEFAULT NULL,
    color_value VARCHAR(255)     DEFAULT NULL,
    price       BIGINT NOT NULL,
    quantity    INT    NOT NULL,
    size_name   VARCHAR(255)     DEFAULT NULL,
    size_value  VARCHAR(255)     DEFAULT NULL,
    product_id  UUID   NOT NULL,
    CONSTRAINT fk_inventories_product FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE product_category
(
    product_id  UUID NOT NULL,
    category_id UUID NOT NULL,
    CONSTRAINT fk_category_product FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT fk_product_category FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE product_payment_method
(
    product_id        UUID NOT NULL,
    payment_method_id UUID NOT NULL,
    CONSTRAINT fk_product_product_payment_method FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_payment_method_product_payment_method FOREIGN KEY (payment_method_id) REFERENCES payment_method (id)
);

CREATE TABLE location
(
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    address          VARCHAR(255) NOT NULL,
    commune          VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP        DEFAULT NOW(),
    default_location BOOLEAN      NOT NULL,
    district         VARCHAR(255) NOT NULL,
    province         VARCHAR(255) NOT NULL,
    updated_at       TIMESTAMP        DEFAULT NOW(),
    user_id          UUID         NOT NULL,
    CONSTRAINT fk_user_location FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE cart
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at  TIMESTAMP        DEFAULT NULL,
    is_payment  BOOLEAN NOT NULL DEFAULT FALSE,
    total_price BIGINT  NOT NULL DEFAULT 0,
    user_id     UUID    NOT NULL,
    seller_id   UUID    NOT NULL,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_cart_seller FOREIGN KEY (seller_id) REFERENCES sellers (id)
);

CREATE TABLE cart_product_inventory
(
    id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    quantity     INT    NOT NULL  DEFAULT 0,
    total_price  BIGINT NOT NULL  DEFAULT 0,
    cart_id      UUID   NOT NULL,
    inventory_id UUID             DEFAULT NULL,
    product_id   UUID             DEFAULT NULL,
    CONSTRAINT fk_cart_product_inventories FOREIGN KEY (cart_id) REFERENCES cart (id) ON DELETE CASCADE,
    CONSTRAINT fk_inventory_product_cart FOREIGN KEY (inventory_id) REFERENCES inventories (id),
    CONSTRAINT fk_product_inventories_cart FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE payment_order
(
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    total_price         BIGINT      NOT NULL,
    payment_method_name VARCHAR(50) NOT NULL,
    delivery_at         TIMESTAMP        DEFAULT NOW(),
    delivery_status     VARCHAR(50) NOT NULL,
    ordered_at          TIMESTAMP        DEFAULT NOW(),
    payment_status      VARCHAR(50) NOT NULL,
    cart_id             UUID        NOT NULL,
    CONSTRAINT fk_payment_order_cart_product_inventory FOREIGN KEY (cart_id) REFERENCES cart (id)
);
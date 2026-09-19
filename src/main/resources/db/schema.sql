CREATE TABLE IF NOT EXISTS users (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  name          VARCHAR(100) NOT NULL,
  email         VARCHAR(150) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  role          VARCHAR(10)  NOT NULL DEFAULT 'BUYER',
  active        BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_users_role CHECK (role IN ('BUYER','SELLER','ADMIN'))
);

CREATE TABLE IF NOT EXISTS products (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  seller_id   BIGINT        NOT NULL,
  name        VARCHAR(150)  NOT NULL,
  description VARCHAR(2000),
  category    VARCHAR(50)   NOT NULL,
  price       DECIMAL(10,2) NOT NULL,
  stock       INT           NOT NULL DEFAULT 0,
  image_url   VARCHAR(300),
  active      BOOLEAN       NOT NULL DEFAULT TRUE,
  created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_products_seller FOREIGN KEY (seller_id) REFERENCES users(id)
);
CREATE INDEX IF NOT EXISTS idx_products_seller ON products(seller_id);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);

CREATE TABLE IF NOT EXISTS orders (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  buyer_id         BIGINT        NOT NULL,
  total_amount     DECIMAL(10,2) NOT NULL,
  status           VARCHAR(20)   NOT NULL DEFAULT 'PLACED',
  shipping_address VARCHAR(500)  NOT NULL,
  created_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_orders_buyer FOREIGN KEY (buyer_id) REFERENCES users(id)
);
CREATE INDEX IF NOT EXISTS idx_orders_buyer ON orders(buyer_id);

CREATE TABLE IF NOT EXISTS order_items (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id   BIGINT        NOT NULL,
  product_id BIGINT        NOT NULL,
  quantity   INT           NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders(id),
  CONSTRAINT fk_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);
CREATE INDEX IF NOT EXISTS idx_items_order ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_items_product ON order_items(product_id);

CREATE TABLE IF NOT EXISTS cart_items (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id    BIGINT    NOT NULL,
  product_id BIGINT    NOT NULL,
  quantity   INT       NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES products(id),
  CONSTRAINT uq_cart_user_product UNIQUE (user_id, product_id)
);
CREATE INDEX IF NOT EXISTS idx_cart_user ON cart_items(user_id);
CREATE INDEX IF NOT EXISTS idx_cart_product ON cart_items(product_id);

CREATE TABLE IF NOT EXISTS reviews (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_id  BIGINT       NOT NULL,
  user_id     BIGINT       NOT NULL,
  rating      INT          NOT NULL,
  review_text VARCHAR(1000),
  created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES products(id),
  CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT chk_reviews_rating CHECK (rating BETWEEN 1 AND 5),
  CONSTRAINT uq_reviews_user_product UNIQUE (user_id, product_id)
);
CREATE INDEX IF NOT EXISTS idx_reviews_product ON reviews(product_id);
CREATE INDEX IF NOT EXISTS idx_reviews_user ON reviews(user_id);

CREATE TABLE order_items
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('AP_UNIQUE_ID'),
    purchase_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity      INTEGER   NOT NULL,
    user_id       BIGINT    NOT NULL,
    product_id    BIGINT    NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);
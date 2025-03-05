CREATE TABLE products
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('AP_UNIQUE_ID'),
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    price       NUMERIC(10, 2) NOT NULL
);
CREATE TABLE ACCOUNT (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   username VARCHAR(50) NOT NULL
);

CREATE TABLE PRODUCT (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR(50) NOT NULL,
   price NUMERIC(12, 2) NOT NULL
);

CREATE TABLE INVENTORY (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   product_id INT NOT NULL,
   quantity INT NOT NULL
);
CREATE UNIQUE INDEX IDX_INV_PID ON Inventory(product_id);

CREATE TABLE CART (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   account_id INT NOT NULL
);
CREATE UNIQUE INDEX IDX_CART_UID ON CART(account_id);

CREATE TABLE CART_ITEM (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   cart_Id INT NOT NULL,
   product_id INT NOT NULL,
   quantity INT NOT NULL
);
CREATE INDEX IDX_CART_ITM_CID ON CART_ITEM(cart_Id);


-- populate some data for test
INSERT INTO Account (username) VALUES ('hong');
INSERT INTO Account (username) VALUES ('hua');

INSERT INTO Product (name, price) VALUES ('iphone 14', 999);
INSERT INTO Product (name, price) VALUES ('iphone 13', 799);
INSERT INTO Product (name, price) VALUES ('iphone 12', 599);





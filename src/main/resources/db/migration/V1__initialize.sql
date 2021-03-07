SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS roles;

CREATE TABLE roles
(
    id   int(11) NOT NULL AUTO_INCREMENT,
    name varchar(50) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

INSERT INTO roles (name)
VALUES ('ROLE_EMPLOYEE'),
       ('ROLE_MANAGER'),
       ('ROLE_ADMIN');

DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         int(11)     NOT NULL AUTO_INCREMENT,
    username   varchar(50) NOT NULL,
    password   char(80)    NOT NULL,
    first_name varchar(50) NOT NULL,
    last_name  varchar(50) NOT NULL,
    email      varchar(50) NOT NULL,
    phone      varchar(15) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

INSERT INTO users (username, password, first_name, last_name, email, phone)
VALUES ('alex', '$2a$10$CGamJWPYokRss2FX6WcBJOgFIDKyUoCYnrGtepfVQyDSTdngSxrzK', 'Alex', 'GeekBrains', 'alex@gb.com',
        '+79263511111');

DROP TABLE IF EXISTS users_roles;

CREATE TABLE users_roles
(
    user_id int(11) NOT NULL,
    role_id int(11) NOT NULL,

    PRIMARY KEY (user_id, role_id),

    KEY FK_ROLE_idx (role_id),

    CONSTRAINT FK_USER_05 FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_ROLE FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (1, 3);

DROP TABLE IF EXISTS categories;

CREATE TABLE categories
(
    id          int(11)       NOT NULL AUTO_INCREMENT,
    title       varchar(255)  NOT NULL,
    description varchar(5000) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARSET = utf8;

DROP TABLE IF EXISTS products;

CREATE TABLE products
(
    id                int(11)       NOT NULL AUTO_INCREMENT,
    category_id       int(11)       NOT NULL,
    vendor_code       VARCHAR(8)    NOT NULL,
    title             varchar(255)  NOT NULL,
    short_description VARCHAR(1000) NOT NULL,
    full_description  VARCHAR(5000) not null,
    price             decimal(8, 2) NOT NULL,
    create_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT FK_CATEGORY_ID FOREIGN KEY (category_id) REFERENCES categories (id)
) ENGINE = InnoDB
  CHARSET = utf8;

DROP TABLE IF EXISTS product_images;

CREATE TABLE product_images
(
    id         int(11)      NOT NULL AUTO_INCREMENT,
    product_id int(11)      NOT NULL,
    path       varchar(250) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FK_PRODUCT_ID_IMG FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE = InnoDB
  CHARSET = utf8;

DROP TABLE IF EXISTS order_statuses;

CREATE TABLE order_statuses
(
    id    int(11)     NOT NULL AUTO_INCREMENT,
    title varchar(50) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARSET = utf8;

DROP TABLE IF EXISTS delivery_addresses;

CREATE TABLE delivery_addresses
(
    id      int(11)       NOT NULL AUTO_INCREMENT,
    user_id int(11)       NOT NULL,
    address varchar(5000) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FK_USER_ID_DEL_ADR FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = InnoDB
  CHARSET = utf8;

DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
    id                  int(11)       NOT NULL AUTO_INCREMENT,
    user_id             int(11)       NOT NULL,
    price               decimal(8, 2) NOT NULL,
    delivery_price      decimal(8, 2) NOT NULL,
    delivery_address_id int(11)       NOT NULL,
    phone_number        VARCHAR(15)   NOT NULL,
    status_id           int(11)       NOT NULL,
    delivery_date       TIMESTAMP     NOT NULL,
    create_at           TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at           TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT FK_USER_ID FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_STATUS_ID FOREIGN KEY (status_id) REFERENCES order_statuses (id),
    CONSTRAINT FK_DEL_ADR_ID FOREIGN KEY (delivery_address_id) REFERENCES delivery_addresses (id)
) ENGINE = InnoDB
  CHARSET = utf8;

DROP TABLE IF EXISTS orders_item;

CREATE TABLE orders_item
(
    id          int(11)       NOT NULL AUTO_INCREMENT,
    product_id  int(11)       NOT NULL,
    order_id    int(11)       NOT NULL,
    quantity    int           not null,
    item_price  decimal(8, 2) NOT NULL,
    total_price decimal(8, 2) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FK_ORDER_ID FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT FK_PROD_ID_ORD_ID FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE = InnoDB
  CHARSET = utf8;

INSERT INTO categories (title, description)
VALUES ('Телевизоры', 'Лучшие телевизоры'),
       ('Ноутбуки', 'То что вам нужно для изучения Java');

INSERT INTO order_statuses (title)
VALUES ('Сформирован');

INSERT INTO products (category_id, vendor_code, title, short_description, full_description, price)
VALUES (1, '00000001', 'Телевизор ViewSonic', 'Хороший телевизор', 'Ну почти хороший телевизор', 3.00);

INSERT INTO product_images (product_id, path)
VALUES (1, '1.jpg');

INSERT INTO delivery_addresses (user_id, address)
VALUES (1, 'Тупой переулок, 17');

SET FOREIGN_KEY_CHECKS = 1;
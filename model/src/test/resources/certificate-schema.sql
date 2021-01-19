CREATE TABLE gift_certificate
(
    id               INT           NOT NULL AUTO_INCREMENT,
    name             VARCHAR(30)   NOT NULL,
    description      VARCHAR(60)   NOT NULL,
    price            DECIMAL(7, 2) NOT NULL,
    duration         INT           NOT NULL,
    create_date      TIMESTAMP     NOT NULL,
    last_update_date TIMESTAMP     NOT NULL,
    PRIMARY KEY (id)
);
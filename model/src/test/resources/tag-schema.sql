CREATE TABLE tag
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(15) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);
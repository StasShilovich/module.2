CREATE TABLE tag_certificate
(
    id_tag         INT         NOT NULL,
    id_certificate VARCHAR(45) NOT NULL,
    PRIMARY KEY (id_tag, id_certificate)
);

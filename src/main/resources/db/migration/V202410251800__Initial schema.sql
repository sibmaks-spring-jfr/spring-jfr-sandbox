CREATE SCHEMA IF NOT EXISTS sandbox;

SET SCHEMA sandbox;


CREATE TABLE IF NOT EXISTS groups
(
    id         bigint       NOT NULL AUTO_INCREMENT,
    code       varchar(128) NOT NULL UNIQUE,
    created_at timestamp    NOT NULL,
    CONSTRAINT group_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id          bigint       NOT NULL AUTO_INCREMENT,
    username    varchar(128) NOT NULL UNIQUE,
    group_id    bigint       NOT NULL,
    created_at  timestamp    NOT NULL,
    modified_at timestamp    NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id),
    FOREIGN KEY (group_id) references groups (id)
);

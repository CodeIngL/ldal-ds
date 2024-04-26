create schema if not exists ds2;

use ds2;

CREATE TABLE if not exists `t_order`
(
    `id`       bigint NOT NULL AUTO_INCREMENT,
    `order_id` int    NOT NULL,
    `user_id`  int    NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;



create schema if not exists ds3;

use ds3;

CREATE TABLE if not exists `t_order`
(
    `id`       bigint NOT NULL AUTO_INCREMENT,
    `order_id` int    NOT NULL,
    `user_id`  int    NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;





DROP USER IF EXISTS 'stream-user'@'%';
CREATE USER 'stream-user'@'%' IDENTIFIED BY 'stream';

CREATE DATABASE IF NOT EXISTS stock_prices CHARACTER SET utf8;
GRANT ALL ON stock_prices.* TO 'stream-user'@'%';

USE stock_prices;
CREATE TABLE IF NOT EXISTS stock_prices (
    window_start BIGINT NOT NULL,
    id varchar(32) NOT NULL,
    symbol varchar(128) NOT NULL,
    PRIMARY KEY (window_start, id)
);

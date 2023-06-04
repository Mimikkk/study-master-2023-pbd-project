DROP USER IF EXISTS 'stream-user'@'%';
CREATE USER 'stream-user'@'%' IDENTIFIED BY 'stream';

CREATE DATABASE IF NOT EXISTS stock_prices CHARACTER SET utf8;
GRANT ALL ON stock_prices.* TO 'stream-user'@'%';

USE stock_prices;
CREATE TABLE IF NOT EXISTS stock_prices (
    window_start BIGINT NOT NULL,
    stock_id varchar(256) NOT NULL,
    title varchar(256) NOT NULL,
    close FLOAT NOT NULL,
    low FLOAT NOT NULL,
    high FLOAT NOT NULL,
    volume INT NOT NULL,
    PRIMARY KEY (window_start, stock_id)
);

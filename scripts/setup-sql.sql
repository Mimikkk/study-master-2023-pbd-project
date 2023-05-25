DROP USER IF EXISTS 'stream-user'@'%';
CREATE USER 'stream-user'@'%' IDENTIFIED BY 'stream';

CREATE DATABASE IF NOT EXISTS stocks CHARACTER SET utf8;
GRANT ALL ON stocks.* TO 'stream-user'@'%';

USE stocks;
CREATE TABLE IF NOT EXISTS stocks (
    window_start BIGINT NOT NULL,
    movie_id varchar(32) NOT NULL,
    title varchar(128) NOT NULL,
    rating_count INTEGER NOT NULL,
    rating_sum INTEGER NOT NULL,
    unique_rating_count INTEGER NOT NULL,
    PRIMARY KEY (window_start, movie_id)
);

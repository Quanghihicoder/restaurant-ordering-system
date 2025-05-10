CREATE DATABASE IF NOT EXISTS db_restaurant;
USE db_restaurant;

CREATE TABLE IF NOT EXISTS user (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(100),
  user_email VARCHAR(100) UNIQUE,
  user_password VARCHAR(255),
  user_phone VARCHAR(20),
  user_birth DATE,
  user_gender VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS food (
  food_id INT AUTO_INCREMENT PRIMARY KEY,
  food_name VARCHAR(100),
  food_price DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS cart (
  user_id INT,
  food_id INT,
  item_qty INT,
  PRIMARY KEY (user_id, food_id)
);

CREATE TABLE IF NOT EXISTS billstatus (
  bill_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  bill_status INT DEFAULT 0,
  bill_paid VARCHAR(10) DEFAULT 'false'
);

CREATE TABLE IF NOT EXISTS billdetails (
  billdetail_id INT AUTO_INCREMENT PRIMARY KEY,
  bill_id INT,
  food_id INT,
  item_qty INT
);

CREATE TABLE IF NOT EXISTS booktable (
  booking_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  table_number INT,
  booking_time DATETIME
);

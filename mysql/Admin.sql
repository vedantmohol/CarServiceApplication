CREATE DATABASE ADMIN;
USE ADMIN;
CREATE TABLE admin_access
(
service_code VARCHAR(5) PRIMARY KEY,
car_type VARCHAR(20),
service_price INT NOT NULL
);

SELECT * FROM admin_access;



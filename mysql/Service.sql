CREATE DATABASE service;
USE service;
CREATE TABLE service_info
(
customer_id INT AUTO_INCREMENT PRIMARY KEY,
customer_name VARCHAR(24),
service_code VARCHAR(5),
car_type VARCHAR(20),
service_price INT NOT NULL
)
SELECT * FROM service_info;
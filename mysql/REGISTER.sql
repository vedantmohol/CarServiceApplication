CREATE DATABASE REGISTER;
USE REGISTER;
show databases;
CREATE TABLE Register_Info
( 
Customer_Name VARCHAR(24),
Customer_Email_ID VARCHAR(30) NOT NULL,
Customer_Password INT NOT NULL
);
SELECT * FROM Register_info;

INSERT INTO Register_info
(Customer_Name,Customer_Email_ID,Customer_Password)
VALUES
("aditya","adityanarsale22@gmail.com",000),
("palaash","palaashpadman22@gmail.com",001);

DROP TABLE Register_info;
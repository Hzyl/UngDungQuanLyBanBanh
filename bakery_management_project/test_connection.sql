-- Test connection: Chạy trong phpMyAdmin để kiểm tra database đã tồn tại chưa
SHOW DATABASES LIKE 'bakerydb';

-- Kiểm tra tables
USE bakerydb;
SHOW TABLES;

-- Kiểm tra dữ liệu employee
SELECT * FROM employee;

-- Create database and tables for Bakery Management
CREATE DATABASE IF NOT EXISTS bakerydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bakerydb;

CREATE TABLE product (
  id VARCHAR(20) PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  description TEXT,
  cost_price DECIMAL(12,2) DEFAULT 0,
  sell_price DECIMAL(12,2) DEFAULT 0,
  quantity INT DEFAULT 0
);

CREATE TABLE customer (
  id VARCHAR(20) PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(100)
);

CREATE TABLE employee (
  id VARCHAR(20) PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  role TINYINT DEFAULT 1 COMMENT '0=Admin, 1=Nhân viên',
  salary DECIMAL(12,2) DEFAULT 0
);

CREATE TABLE orders (
  id VARCHAR(20) PRIMARY KEY,
  customer_id VARCHAR(20),
  employee_id VARCHAR(20),
  order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(20) DEFAULT 'Pending',
  FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE SET NULL,
  FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE SET NULL
);

CREATE TABLE order_item (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id VARCHAR(20),
  product_id VARCHAR(20),
  quantity INT,
  unit_price DECIMAL(12,2),
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE SET NULL
);

-- Sample data
INSERT INTO product (id,name,description,cost_price,sell_price,quantity) VALUES
('P001','Bánh mì Danish','Danish bơ thơm ngon',20000,35000,50),
('P002','Bánh chocolate','Bánh chocolate mềm',25000,40000,40),
('P003','Bánh croissant','Croissant bơ Pháp',18000,32000,60),
('P004','Bánh tiramisu','Tiramisu kem phô mai',35000,65000,25),
('P005','Bánh bông lan','Bông lan trứng tươi',15000,28000,80),
('P006','Bánh kem dâu','Bánh kem dâu tây tươi',40000,75000,20),
('P007','Bánh mì que','Bánh mì que giòn tan',8000,15000,100),
('P008','Bánh macaron','Macaron nhiều vị',30000,55000,35),
('P009','Bánh su kem','Su kem nhân kem tươi',12000,22000,70),
('P010','Bánh tart trái cây','Tart trái cây tươi',28000,50000,30);

INSERT INTO customer (id,name,phone,email) VALUES
('C001','Nguyễn Văn A','0909123456','nguyenvana@gmail.com'),
('C002','Trần Thị B','0912345678','tranthib@gmail.com'),
('C003','Lê Hoàng C','0923456789','lehoangc@gmail.com'),
('C004','Phạm Thị D','0934567890','phamthid@gmail.com'),
('C005','Võ Văn E','0945678901','vovane@gmail.com'),
('C006','Hoàng Thị F','0956789012','hoangthif@gmail.com'),
('C007','Đặng Văn G','0967890123','dangvang@gmail.com'),
('C008','Bùi Thị H','0978901234','buithih@gmail.com'),
('C009','Dương Văn I','0989012345','duongvani@gmail.com'),
('C010','Ngô Thị K','0901234567','ngothik@gmail.com');

INSERT INTO employee (id,name,username,password,role,salary) VALUES
('E001','Quản trị viên','admin','admin',0,15000000),
('E002','Nguyễn Văn Nam','nhanvien','123456',1,8000000),
('E003','Trần Thị Mai','maitn','123456',1,7500000),
('E004','Lê Văn Hùng','hunglv','123456',1,8500000),
('E005','Phạm Thị Lan','lanpt','123456',1,7500000),
('E006','Võ Minh Tuấn','tuanvm','123456',0,12000000),
('E007','Hoàng Thị Nga','nhaht','123456',1,7800000),
('E008','Đặng Văn Long','longdv','123456',1,8500000),
('E009','Bùi Thị Hương','huongbt','123456',1,7500000),
('E010','Dương Văn Tùng','tungdv','123456',1,6500000);

INSERT INTO orders (id,customer_id,employee_id,order_date,status) VALUES
('O001','C001','E002','2024-11-01 09:30:00','Completed'),
('O002','C002','E002','2024-11-02 10:15:00','Completed'),
('O003','C003','E004','2024-11-03 14:20:00','Completed'),
('O004','C004','E002','2024-11-05 11:45:00','Completed'),
('O005','C005','E004','2024-11-08 16:30:00','Completed'),
('O006','C001','E002','2024-11-10 08:50:00','Completed'),
('O007','C006','E004','2024-11-12 13:20:00','Completed'),
('O008','C007','E002','2024-11-15 10:10:00','Completed'),
('O009','C008','E004','2024-11-16 15:40:00','Pending'),
('O010','C009','E002','2024-11-18 09:25:00','Pending');

INSERT INTO order_item (order_id,product_id,quantity,unit_price) VALUES
('O001','P001',2,35000),
('O001','P003',1,32000),
('O002','P002',3,40000),
('O002','P007',5,15000),
('O003','P004',1,65000),
('O003','P006',1,75000),
('O004','P005',4,28000),
('O004','P009',3,22000),
('O005','P008',2,55000),
('O005','P010',1,50000),
('O006','P001',3,35000),
('O006','P007',10,15000),
('O007','P002',2,40000),
('O007','P003',2,32000),
('O008','P004',2,65000),
('O008','P006',1,75000),
('O009','P005',5,28000),
('O009','P009',4,22000),
('O010','P001',1,35000),
('O010','P008',3,55000);

Bakery Management Project - Đồ án Quản lý cửa hàng bánh
=========================================================

ĐỒ ÁN MÔN HỌC: Lập trình Java
Hệ thống quản lý cửa hàng bánh với Swing GUI và MySQL Database

TÍNH NĂNG:
----------
1. Đăng nhập với phân quyền:
   - Tài khoản Admin: Truy cập toàn bộ chức năng
   - Tài khoản Nhân viên: Quản lý sản phẩm, đơn hàng, khách hàng

2. Quản lý sản phẩm:
   - Thêm, sửa, xóa sản phẩm
   - Tìm kiếm sản phẩm theo tên
   - Quản lý giá vốn, giá bán, tồn kho

3. Quản lý đơn hàng:
   - Tạo đơn hàng mới
   - Thêm sản phẩm vào đơn
   - Tự động trừ tồn kho khi hoàn thành đơn
   - Sử dụng transaction để đảm bảo tính toàn vẹn dữ liệu

4. Quản lý khách hàng:
   - Lưu trữ thông tin khách hàng
   - Tìm kiếm theo tên

5. Quản lý nhân viên (Chỉ Admin):
   - Thêm, sửa, xóa tài khoản nhân viên
   - Quản lý username, password, vai trò

6. Báo cáo doanh thu (Chỉ Admin):
   - Xem danh sách đơn đã hoàn thành
   - Thống kê tổng tiền từng đơn

CÀI ĐẶT:
--------
1. Tạo database MySQL:
   mysql -u root -p < db_init.sql

2. Cấu hình kết nối database (nếu cần):
   Sửa file: src/main/java/com/mycompany/bakery/util/DBUtil.java
   - URL: jdbc:mysql://localhost:3306/bakerydb
   - USER: root
   - PASS: root

3. Build project:
   mvn clean package

4. Chạy ứng dụng:
   java -jar target/bakery-management-1.0-SNAPSHOT.jar

TÀI KHOẢN MẶC ĐỊNH:
-------------------
Admin:
  - Username: admin
  - Password: admin
  - Quyền: Truy cập toàn bộ chức năng

Nhân viên:
  - Username: nhanvien
  - Password: 123456
  - Quyền: Quản lý sản phẩm, đơn hàng, khách hàng

CÔNG NGHỆ SỬ DỤNG:
------------------
- Java 17
- Swing GUI
- MySQL Database
- JDBC
- Maven

GHI CHÚ:
--------
- Hệ thống sử dụng transaction khi tạo đơn hàng để đảm bảo:
  + Lưu thông tin đơn hàng
  + Lưu chi tiết sản phẩm
  + Trừ tồn kho tự động
  + Rollback nếu có lỗi

- Phân quyền được kiểm tra ngay khi đăng nhập và ẩn/hiện menu theo vai trò

CẤU TRÚC PROJECT:
----------------
- models: Product, Customer, Employee, Order, OrderItem
- dao: ProductDAO, CustomerDAO, EmployeeDAO, OrderDAO
- ui: LoginDialog, MainFrame
- ui.panels: DashboardPanel, ProductPanel, OrderPanel, CustomerPanel, EmployeePanel, RevenuePanel
- util: DBUtil (kết nối database)

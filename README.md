# 📚 Bookstore Web Application

## 📌 Giới thiệu

**Bookstore Web Application** là một website bán sách trực tuyến được xây dựng bằng **Java Servlet/JSP (Maven)** theo mô hình **MVC**.
Hệ thống cho phép người dùng duyệt sách, mua hàng và quản trị viên quản lý toàn bộ hệ thống.

---

## 🚀 Công nghệ sử dụng

* ☕ Java Servlet & JSP
* 📦 Maven
* 🗄️ JDBC (SQL Server)
* 🔐 jBCrypt (mã hóa mật khẩu)
* 🎨 HTML, CSS
* 🧱 MVC Architecture

---

## 📂 Cấu trúc project

```
bookstoreweb/
│── src/
│   ├── main/
│   │   ├── java/com/bookstore/
│   │   │   ├── controller/        # Xử lý request (Servlet)
│   │   │   ├── controller/admin/  # Admin controller
│   │   │   ├── dao/               # Data Access Object
│   │   │   ├── model/             # Entity classes
│   │   │   └── util/              # Utility (DB connection)
│   │   ├── resources/             # Config (db.properties)
│   │   └── webapp/
│   │       ├── css/               # Styles
│   │       ├── images/            # Images
│   │       ├── views/             # JSP pages
│   │       └── WEB-INF/
│   │           └── web.xml
│
│── pom.xml
│── README.md
```

---

## ✨ Chức năng chính

### 👤 Người dùng

* Đăng ký / Đăng nhập
* Xem danh sách sách
* Xem chi tiết sách
* Thêm vào giỏ hàng
* Cập nhật / xóa giỏ hàng
* Thanh toán (checkout)
* Xem lịch sử đơn hàng
* Đổi mật khẩu

---

### 🛠️ Quản trị viên (Admin)

* Quản lý sách (CRUD)
* Quản lý người dùng
* Quản lý đơn hàng
* Xem dashboard thống kê

---

## ⚙️ Cài đặt & chạy project

### 1. Clone project

```bash
git clone https://github.com/your-username/your-repo.git
```

---

### 2. Cấu hình database

Tạo file:

```
src/main/resources/db.properties
```

Ví dụ:

```properties
db.url=jdbc:sqlserver://localhost:1433;databaseName=BookStore
db.user=your_username
db.password=your_password
```

---

### 3. Build project

```bash
mvn clean install
```

---

### 4. Deploy lên server

* Sử dụng Apache Tomcat
* Deploy file `.war` trong thư mục `target`

---

## 📌 Ghi chú

* Thư mục `target/` đã được ignore (không push lên Git)
* File `db.properties` không nên public (chứa thông tin DB)
* Sử dụng BCrypt để mã hóa mật khẩu

---

## 👨‍💻 Tác giả

* Nguyễn Hải Long
* Email: hailongproptit@gmail.com
* GitHub: https://github.com/LongNH-230503

---

## ⭐ Đánh giá

Nếu bạn thấy project hữu ích, hãy ⭐ repo nhé!

---

## 📜 License

This project is for educational purposes.

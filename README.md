
# 📚 Backend - Hệ Thống Quản Lý Giáo Dục

Đây là hệ thống backend cho nền tảng quản lý giáo dục, được xây dựng bằng **Spring Boot**, **WebFlux** và **MongoDB**, hỗ trợ lập trình phản ứng (reactive) và bảo mật bằng JWT.

---

## 🚀 Tính Năng Chính

- 🔐 Xác thực & phân quyền bằng **JWT**
- 👥 Quản lý **người dùng**, **vai trò** (Học sinh, Giáo viên)
- 🧾 Theo dõi **lớp học**, **giảng dạy**, **lịch học**
- ⚡ Hỗ trợ xử lý **bất đồng bộ** với Spring WebFlux
- 🧩 Tích hợp cả **Reactive** và **Blocking** MongoDB Repositories
- ☁️ Tích hợp gửi mail, Cloudinary và AI (Gemini API)

---

## 🛠️ Công Nghệ Sử Dụng

| Thành phần       | Công nghệ                        |
|------------------|----------------------------------|
| Ngôn ngữ         | Java 17+                         |
| Framework        | Spring Boot 3, WebFlux           |
| Bảo mật          | Spring Security + JWT            |
| Cơ sở dữ liệu    | MongoDB (Reactive + Blocking)    |
| Quản lý dự án    | Maven                            |
| Gửi mail         | SMTP qua Gmail                   |
| Lưu trữ ảnh      | Cloudinary API                   |
| Trí tuệ nhân tạo | Gemini API                       |

---

## 🏗️ Cấu Trúc Dự Án (Tổng quan)

```
src/
├── config/                # Cấu hình bảo mật, JWT
├── controller/            # API endpoints
├── dto/                   # Các lớp chuyển dữ liệu
├── entity/                # Định nghĩa dữ liệu MongoDB
├── repository/            # Reactive + Blocking Repositories
├── service/               # Business logic
├── util/                  # Các hàm hỗ trợ (utility)
├── application.yaml       # Cấu hình ứng dụng
```

---

## ⚙️ Hướng Dẫn Chạy Dự Án

### 📌 Yêu cầu hệ thống

- Java 17+
- Maven
- MongoDB
- (Tuỳ chọn) Docker

### ▶️ Chạy bằng Maven

```bash
# Cài đặt và build
mvn clean install

# Hoặc chỉ build không chạy test
mvn clean package -DskipTests

# Chạy app
mvn spring-boot:run
```

---

### 🐳 Chạy bằng Docker (khuyến khích cho tester/dev)

#### Bước 1: Tạo file `.env` (hoặc dùng file `.env.example`)

```bash
cp .env.example .env
```
> 👉 Rồi chỉnh lại thông tin thật trong file `.env`

```
   Có thể sử dụng application.yaml để cấu hình, thêm các giá trị tương ứng trong file .env.example
   vào biến môi trường trên máy tinh
```
#### Bước 2: Chạy Docker

```bash
docker-compose up --build
```

---

## 🧪 Kiểm Thử Với Postman

- Base URL: `http://localhost:8080`
- Đảm bảo bạn đã cấu hình `.env` đầy đủ trước khi test các API
- Các endpoint cần JWT sẽ yêu cầu bạn đăng nhập để lấy token
- Chỉ cần import file [Backend Edu.postman_collection.json](Backend%20Edu.postman_collection.json) có sẵn trong thư mục dự án vào Postman
---

## 📁 Biến Môi Trường

Các biến được cấu hình qua `.env`:

```env
MAIL_USERNAME=
MAIL_PASSWORD=
JWT_SECRET_KEY=
CLOUDINARY_CLOUD_NAME=
CLOUDINARY_API_KEY=
CLOUDINARY_API_SECRET=
GEMINI_API_KEY=
```

> ⚠️ Không commit file `.env` thật — chỉ commit `.env.example` để hướng dẫn

---

## 🤝 Đóng Góp

Mọi đóng góp đều được hoan nghênh! Hãy tạo pull request hoặc issue nếu bạn phát hiện lỗi hoặc muốn thêm tính năng mới.

---

## 📄 Giấy Phép

MIT License
```

---


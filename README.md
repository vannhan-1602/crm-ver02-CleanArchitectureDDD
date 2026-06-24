# CRM Online — Ver 02

Hệ thống CRM cho doanh nghiệp kinh doanh giải pháp phần mềm & dịch vụ CNTT, xây dựng theo kiến trúc **Clean Architecture + DDD + CQRS**. Backend Spring Boot (đa module Maven), frontend React + Vite (SPA).

---

## 1. Tình trạng hiện tại (đã làm được gì)

### Đã hoàn thành — CRUD + nghiệp vụ cơ bản

| Module | Mô tả |
|---|---|
| **Lead** | CRUD, đổi trạng thái (`/status`), chuyển đổi Lead → Khách hàng/Cơ hội (`/convert`) |
| **Khách hàng** | CRUD, lọc theo loại/tình trạng, xóa mềm (soft delete) |
| **Cơ hội bán hàng** | CRUD đầy đủ |
| **Hoạt động chăm sóc khách hàng** | CRUD đầy đủ |
| **Báo giá** | Tạo kèm chi tiết dòng hàng, CRUD |
| **Hợp đồng** | CRUD, đánh dấu hoàn tất (`/complete`) |
| **Hóa đơn / Phiếu thu / Phiếu chi** | CRUD đầy đủ cho cả 3 |
| **Sản phẩm & Loại sản phẩm** | CRUD, upload/xóa hình ảnh sản phẩm (multipart) |
| **Ticket hỗ trợ** | CRUD ticket, loại ticket, phản hồi ticket |
| **Nhân viên** | Xem danh sách (chưa có thêm/sửa/xóa) |
| **Báo cáo thống kê** | Có endpoint trả dữ liệu thống kê + trang giao diện riêng |
| **Đăng nhập / Đăng xuất** | Có trang Login, lưu token + thông tin user/permissions ở localStorage |
| **Phân quyền theo module** | Admin/Manager có thể bật/tắt quyền Xem–Đọc–Sửa cho từng user theo từng module nghiệp vụ (Lead, Khách hàng, Hợp đồng...); sidebar và route ở frontend tự ẩn/hiện theo quyền |

Tài khoản mẫu có sẵn trong schema (xem mục 5):

| Username | Password | Role |
|---|---|---|
| `admin` | `123456` | Admin (toàn quyền) |
| `manager` | `123456` | Manager |
| `sale01` | `123456` | Sale |
| `ketoan01` | `123456` | Accountant |

### Việc còn lại 
- check nghiệp vụ 
- sửa các frontend cần thiết
- loại bỏ như folder không cần thiết 


## 2. Cấu trúc dự án

```
crm-ver02-CleanArchitectureDDD/
├── crm-domain/          # Entities, domain interfaces (không phụ thuộc layer nào)
├── crm-application/     # Use cases: Commands, Queries, Handlers, Mediator (gồm cả auth/ — chưa dùng tới)
├── crm-persistence/     # JPA entities, Spring Data repositories, Adapter implements
├── crm-infrastructure/  # Cấu hình chung, beans Spring
├── crm-presentation/    # REST Controllers theo Clean Architecture + Spring Security (JWT) — auth/permission chưa được nối với frontend
├── crm-api/             # Entry point (CrmApplication, application.yml) + bộ Auth/Permission đang dùng thật (AuthController, AdminUserController, AuthService, TokenService...)
├── db/crmo.sql          # Schema SQL mới nhất — dùng file này để import
├── uploads/products/    # Ảnh sản phẩm đã upload (lưu trên đĩa, serve qua /uploads/**)
└── frontend/             # React + Vite SPA
    └── src/
        ├── App.jsx           # Router + sidebar + áp dụng phân quyền theo module
        ├── apiClient.js      # Axios instance, lưu token/permissions vào localStorage
        └── page/             # LoginPage, LeadManager, KhachHangManager, CoHoiManager,
                                HoatDongManager, BaoGia, HopDong, TaiChinh, SanPhamManager,
                                TicketManager, BaoCaoThongKe, UserPermissionManager
```

### Luồng phụ thuộc (Clean Architecture — áp dụng cho phần CRUD nghiệp vụ)

```
Presentation → Application → Domain ← Persistence
```

- **Domain**: không import gì từ layer khác.
- **Application**: chỉ biết Domain.
- **Persistence / Presentation**: implement / sử dụng interfaces của Domain.
- *(Riêng phần Auth/Permission hiện đang đi tắt qua `crm-api`, xem mục 1.)*

---

## 3. Yêu cầu môi trường

| Công cụ | Phiên bản tối thiểu |
|---|---|
| JDK | 21 |
| Maven | 3.9+ |
| MySQL | 8.0+ |
| Node.js | 18+ |
| npm | 9+ |

---

## 4. Hướng dẫn chạy dự án (step by step)

### Bước 1 — Clone repository

```bash
git clone https://github.com/vannhan-1602/crm-ver02-CleanArchitectureDDD
cd crm-ver02-CleanArchitectureDDD
```

### Bước 2 — Tạo database MySQL

```sql
CREATE DATABASE CRMOnline
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

Import schema mới nhất (bảng + dữ liệu mẫu, gồm 4 tài khoản đăng nhập ở mục 1):

```bash
mysql -u root -p CRMOnline < db/crmo.sql
```

> Không dùng `CRMOnline.sql` ở thư mục gốc — file đó là bản cũ, thiếu bảng.

### Bước 3 — Cấu hình kết nối database

Mặc định `crm-api/src/main/resources/application.yml` đọc từ biến môi trường (có fallback sẵn để chạy local không cần set gì):

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/CRMOnline?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:123456}
```

- Nếu MySQL local của bạn dùng user/password khác `root`/`123456`, set biến môi trường trước khi chạy:
  ```bash
  export DB_USERNAME=your_user
  export DB_PASSWORD=your_password
  ```
- `ddl-auto: none` — Hibernate **không** tự tạo/sửa bảng, schema phải lấy từ `db/crmo.sql`.

### Bước 4 — Build và chạy Backend

```bash
# Đứng ở thư mục gốc của project
mvn clean install -DskipTests

# Chạy Spring Boot
mvn spring-boot:run -pl crm-api
```

Backend chạy tại: **http://localhost:8081**

Kiểm tra nhanh:

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

> **Lưu ý IntelliJ:** có thể chạy thẳng class `CrmApplication` trong module `crm-api` bằng nút ▶ Run.

### Bước 5 — Cài dependencies và chạy Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend chạy tại: **http://localhost:5173**

> Nếu backend chạy ở port khác `8081`, tạo file `frontend/.env.local`:
> ```
> VITE_API_BASE_URL=http://localhost:<port>
> ```

### Bước 6 — Đăng nhập và sử dụng

Mở **http://localhost:5173**, đăng nhập với một trong 4 tài khoản ở mục 1 (vd. `admin` / `123456`), sau đó điều hướng qua sidebar — các mục hiển thị sẽ tùy theo quyền của user đó:

| Menu | Route | Module key |
|---|---|---|
| Quản lý Lead | `/leads` | `LEAD` |
| Quản lý Khách hàng | `/khach-hang` | `KHACH_HANG` |
| Quản lý Cơ hội bán hàng | `/cohoi` | `CO_HOI` |
| Quản lý Hoạt động | `/hoat-dong` | `HOAT_DONG` |
| Quản lý Báo giá | `/baogia` | `BAO_GIA` |
| Quản lý Hợp đồng | `/hop-dong` | `HOP_DONG` |
| Quản lý Hóa đơn | `/tai-chinh` | `HOA_DON`, `TAI_CHINH` |
| Quản lý Sản phẩm | `/sanpham` | `SAN_PHAM` |
| Quản lý Ticket | `/tickets` | `TICKET` |
| Báo cáo thống kê | `/bao-cao-thong-ke` | `BAO_CAO` |
| Quản lý người dùng & phân quyền | `/users` | (chỉ Admin/Manager) |

---

## 5. Danh sách API Endpoints

### Auth (`/api/auth`) — đang dùng thật, nằm ở `crm-api`

| Method | Endpoint | Mô tả |
|---|---|---|
| POST | `/api/auth/login` | Đăng nhập, trả về token + user + permissions |
| POST | `/api/auth/logout` | Đăng xuất |
| GET | `/api/auth/me` | Lấy thông tin user hiện tại + quyền |

### Quản lý người dùng & phân quyền (`/api/admin`) — đang dùng thật

| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/api/admin/modules` | Danh sách module nghiệp vụ để gán quyền |
| GET | `/api/admin/users` | Danh sách user |
| POST | `/api/admin/users` | Tạo user mới |
| GET | `/api/admin/users/{id}/permissions` | Quyền hiện tại của 1 user |
| PUT | `/api/admin/users/{id}/permissions` | Cập nhật quyền Xem/Đọc/Sửa theo module |

### Lead (`/api/leads`)

| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/api/leads` | Danh sách lead |
| GET | `/api/leads/{id}` | Lead theo ID |
| POST | `/api/leads` | Tạo lead mới |
| PUT | `/api/leads/{id}` | Cập nhật lead |
| DELETE | `/api/leads/{id}` | Xóa lead |
| PATCH | `/api/leads/{id}/status` | Đổi trạng thái lead |
| POST | `/api/leads/{id}/convert` | Chuyển đổi lead thành khách hàng/cơ hội |

### Khách hàng (`/api/khach-hang`)

| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/api/khach-hang` | Danh sách (`?loai=1` để lọc) |
| GET | `/api/khach-hang/{id}` | Khách hàng theo ID |
| POST | `/api/khach-hang` | Tạo khách hàng mới |
| PUT | `/api/khach-hang/{id}` | Cập nhật khách hàng |
| DELETE | `/api/khach-hang/{id}` | Xóa mềm khách hàng |

### Cơ hội bán hàng (`/api/cohoi`)

| Method | Endpoint | Mô tả |
|---|---|---|
| GET/POST/PUT/DELETE | `/api/cohoi`, `/api/cohoi/{id}` | CRUD cơ hội bán hàng |

### Hoạt động chăm sóc (`/api/hoat-dong`)

| Method | Endpoint | Mô tả |
|---|---|---|
| GET/POST/PUT/DELETE | `/api/hoat-dong`, `/api/hoat-dong/{id}` | CRUD hoạt động chăm sóc khách hàng |

### Báo giá (`/api/bao-gia`)

| Method | Endpoint | Mô tả |
|---|---|---|
| POST | `/api/bao-gia` | Tạo báo giá (kèm chi tiết dòng hàng) |
| GET | `/api/bao-gia` | Danh sách báo giá |
| GET | `/api/bao-gia/{id}` | Báo giá theo ID |
| PUT | `/api/bao-gia/{id}` | Cập nhật |
| DELETE | `/api/bao-gia/{id}` | Xóa |

### Hợp đồng (`/api/hop-dong`)

| Method | Endpoint | Mô tả |
|---|---|---|
| POST | `/api/hop-dong` | Tạo hợp đồng |
| GET | `/api/hop-dong` | Danh sách hợp đồng |
| GET | `/api/hop-dong/{id}` | Hợp đồng theo ID |
| PUT | `/api/hop-dong/{id}` | Cập nhật |
| DELETE | `/api/hop-dong/{id}` | Xóa |
| PATCH | `/api/hop-dong/{id}/complete` | Đánh dấu hoàn tất hợp đồng |

### Tài chính

| Method | Endpoint | Mô tả |
|---|---|---|
| GET/POST/PUT/DELETE | `/api/hoa-don`, `/api/hoa-don/{id}` | Hóa đơn |
| GET/POST/PUT/DELETE | `/api/phieu-thu`, `/api/phieu-thu/{id}` | Phiếu thu |
| GET/POST/PUT/DELETE | `/api/phieu-chi`, `/api/phieu-chi/{id}` | Phiếu chi |
| GET | `/api/tai-chinh/thong-ke` | Thống kê tổng hợp tài chính |

### Báo cáo thống kê

| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/api/bao-cao-thong-ke` | Dữ liệu báo cáo thống kê |

### Sản phẩm

| Method | Endpoint | Mô tả |
|---|---|---|
| GET/POST/PUT/DELETE | `/api/sanpham`, `/api/sanpham/{id}` | Sản phẩm |
| POST | `/api/sanpham/{id}/hinhanh` | Upload hình ảnh sản phẩm (multipart) |
| DELETE | `/api/sanpham/{sanPhamId}/hinhanh/{hinhAnhId}` | Xóa hình ảnh sản phẩm |
| GET/POST/PUT/DELETE | `/api/loaisanpham`, `/api/loaisanpham/{id}` | Loại sản phẩm |

### Ticket hỗ trợ

| Method | Endpoint | Mô tả |
|---|---|---|
| GET/POST/PUT/DELETE | `/api/tickets`, `/api/tickets/{id}` | Ticket |
| GET/POST/PUT/DELETE | `/api/loai-ticket`, `/api/loai-ticket/{id}` | Loại ticket |
| GET/POST/DELETE | `/api/ticket-phan-hoi`, `/api/ticket-phan-hoi/{id}` | Phản hồi ticket |

### Nhân viên

| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/api/nhan-vien` | Danh sách nhân viên (chưa có thêm/sửa/xóa) |

### Chưa được frontend sử dụng (viết theo Clean Architecture đầy đủ, nằm ở `crm-presentation`)

| Method | Endpoint | Mô tả |
|---|---|---|
| GET/PUT | `/api/users`, `/api/users/{id}`, `/api/users/{id}/permissions` | Bản Mediator-based, song song với `/api/admin` |
| GET | `/api/permissions/modules` | Bản Mediator-based, song song với `/api/admin/modules` |

---

## 6. Troubleshooting

**Backend không start — lỗi kết nối DB**
- Kiểm tra MySQL đang chạy: `mysql -u root -p`
- Kiểm tra đúng tên DB `CRMOnline` (phân biệt hoa/thường)
- Đảm bảo `DB_USERNAME`/`DB_PASSWORD` (hoặc giá trị mặc định `root`/`123456`) đúng với máy bạn

**Lỗi compile `does not override abstract method`**
- Chạy `mvn clean` rồi build lại
- Kiểm tra tất cả method trong interface domain đã được implement ở adapter tương ứng trong `crm-persistence`

**Đăng nhập không được / 401 ở mọi API**
- Kiểm tra đang gọi đúng `/api/auth/login` (không phải route đang bị comment trong `crm-presentation`)
- Token lưu ở `localStorage` key `crm_token` — xóa localStorage và đăng nhập lại nếu nghi bị token cũ/hỏng

**Frontend lỗi CORS**
- Backend đã cấu hình CORS mở (`@CrossOrigin(origins = "*")` + `CorsFilter`), không cần cấu hình thêm
- Nếu vẫn lỗi, kiểm tra `VITE_API_BASE_URL` có đúng port không

**Frontend không thấy data**
- Mở DevTools → Network, kiểm tra request có trả về 200 không
- Kiểm tra backend đang chạy đúng port (mặc định `8081`)

**Upload ảnh sản phẩm lỗi**
- Giới hạn file 10MB (`max-file-size`/`max-request-size` trong `application.yml`)
- Ảnh được lưu vào thư mục `uploads/products/` tại thư mục gốc dự án, serve qua `/uploads/**` — đảm bảo app có quyền ghi vào thư mục này

---

## 7. Build production

```bash
# Build frontend
cd frontend
npm run build
# Output tại frontend/dist/

# Build backend JAR
cd ..
mvn clean package -DskipTests
# Output tại crm-api/target/crm-api-1.0-SNAPSHOT.jar

# Chạy JAR
java -jar crm-api/target/crm-api-1.0-SNAPSHOT.jar
```
# CRM Online — Ver 02

Hệ thống quản lý khách hàng (CRM) xây dựng theo kiến trúc \*\*Clean Architecture + DDD\*\*, gồm backend Spring Boot và frontend React + Vite.

Hiện tại đã làm xong các module : CRUD : Lead, Khách hàng, Hoạt động chăm sóc khách hàng, hợp đồng,Báo giá Hóa đơn/Phiếu thu/Phiếu chi

---

## Cấu trúc dự án

```

crm-ver02-CleanArchitectureDDD/

├── crm-domain/          # Entitiess, domain interfaces (không phụ thuộc gì)

├── crm-application/     # Use cases: Commands, Queries, Handlers, Mediator

├── crm-persistence/     # JPA entities, Spring Data repos, Adapter impls

├── crm-infrastructure/  # Config chung, beans cấu hình Spring

├── crm-presentation/    # REST Controllers (@RestController)

├── crm-api/             # Entry point: CrmApplication, application.yml

└── frontend/            # React + Vite SPA

   └── src/

       ├── components/  # LeadManager

       └── page/        # HopDong, TaiChinh, SanPhamManager, KhachHangManager

```

### Luồng phụ thuộc (Clean Architecture)

```

Presentation → Application → Domain ← Persistence

```

- \*\*Domain\*\*: không import gì từ các layer khác.
- \*\*Application\*\*: chỉ biết Domain.
- \*\*Persistence / Presentation\*\*: implement / sử dụng interfaces của Domain.

---

## Yêu cầu môi trường

| Công cụ | Phiên bản tối thiểu |

|---------|---------------------|

| JDK | 21 |

| Maven | 3.9+ |

| MySQL | 8.0+ |

| Node.js | 18+ |

| npm | 9+ |

---

## Hướng dẫn chạy dự án

### Bước 1 — Clone repository

```bash

git clone <https://github.com/vannhan-1602/crm-ver02-CleanArchitectureDDD>

cd crm-ver02-CleanArchitectureDDD

```

---

### Bước 2 — Tạo database MySQL

Mở MySQL client (Workbench, DBeaver, hoặc terminal) và chạy:

```sql

CREATE DATABASE CRMOnline

 CHARACTER SET utf8mb4

 COLLATE utf8mb4\_unicode\_ci;

```

> Nếu đã có file SQL schema của nhóm, import thêm:

> mysql -u root -p CRMOnline < schema.sql

---

### Bước 3 — Cấu hình kết nối database

Mở file `crm-api/src/main/resources/application.yml` và chỉnh thông tin cho khớp máy của bạn:

```yaml

spring:

 datasource:

   url: jdbc:mysql://localhost:3306/CRMOnline?useSSL=false\&serverTimezone=UTC\&allowPublicKeyRetrieval=true

   username: root          # ← đổi nếu username MySQL khác

   password: 123456        # ← đổi thành password thực của bạn

 jpa:

   hibernate:

     ddl-auto: none        # ← đổi thành "update" nếu muốn Hibernate tự tạo bảng

```

---

### Bước 4 — Build và chạy Backend

```bash

# Đứng ở thư mục gốc của project

mvn clean install -DskipTests



# Chạy Spring Boot

mvn spring-boot:run -pl crm-api

```

Backend sẽ khởi động tại: \*\*http://localhost:8081\*\*

Kiểm tra nhanh:

```bash

curl http://localhost:8081/api/khach-hang

```

> \*\*Lưu ý IntelliJ:\*\* Có thể chạy thẳng class `CrmApplication` trong module `crm-api` bằng nút ▶ Run.

---

### Bước 5 — Cài dependencies và chạy Frontend

```bash

cd frontend

npm install

npm run dev

```

Frontend sẽ chạy tại: \*\*http://localhost:5173\*\*

> Nếu backend chạy trên port khác 8081, tạo file `frontend/.env.local`:

> VITE\_API\_BASE\_URL=http://localhost:`<port>`

---

### Bước 6 — Truy cập ứng dụng

Mở trình duyệt vào \*\*http://localhost:5173\*\* và điều hướng qua sidebar:

| Menu | Route | Mô tả |

|------|-------|-------|

| Quản lý Lead | `/leads` | Quản lý Lead, đổi trạng thái, chuyển đổi |

| Quản lý Khách hàng | `/khach-hang` | CRUD khách hàng, lọc theo loại \& tình trạng |

| Quản lý Hợp đồng | `/hop-dong` | Tạo, cập nhật, xóa hợp đồng |

| Quản lý Hóa đơn | `/tai-chinh` | Hóa đơn, phiếu thu, phiếu chi |

| Quản lý Sản phẩm | `/sanpham` | Sản phẩm, loại sản phẩm, hình ảnh |

---

## Danh sách API Endpoints

### Lead

| Method | Endpoint | Mô tả |

|--------|----------|-------|

| GET | `/api/leads` | Lấy danh sách lead |

| GET | `/api/leads/{id}` | Lấy lead theo ID |

| POST | `/api/leads` | Tạo lead mới |

| PUT | `/api/leads/{id}` | Cập nhật lead |

| DELETE | `/api/leads/{id}` | Xóa lead |

### Khách hàng

| Method | Endpoint | Mô tả |

|--------|----------|-------|

| GET | `/api/khach-hang` | Lấy danh sách (`?loai=1` để lọc) |

| GET | `/api/khach-hang/{id}` | Lấy khách hàng theo ID |

| POST | `/api/khach-hang` | Tạo khách hàng mới |

| PUT | `/api/khach-hang/{id}` | Cập nhật khách hàng |

| DELETE | `/api/khach-hang/{id}` | Soft delete khách hàng |

### Hợp đồng

| Method | Endpoint | Mô tả |

|--------|----------|-------|

| GET | `/api/hop-dong` | Danh sách hợp đồng |

| GET | `/api/hop-dong/{id}` | Hợp đồng theo ID |

| POST | `/api/hop-dong` | Tạo hợp đồng |

| PUT | `/api/hop-dong/{id}` | Cập nhật |

| DELETE | `/api/hop-dong/{id}` | Xóa |

### Báo giá

| Method | Endpoint | Mô tả |

|--------|----------|-------|

| GET | `/api/bao-gia` | Danh sách báo giá |

| POST | `/api/bao-gia` | Tạo báo giá (kèm chi tiết dòng hàng) |

| PUT | `/api/bao-gia/{id}` | Cập nhật |

| DELETE | `/api/bao-gia/{id}` | Xóa |

### Tài chính

| Method | Endpoint | Mô tả |

|--------|----------|-------|

| GET/POST/PUT/DELETE | `/api/hoa-don` | Hóa đơn |

| GET/POST/PUT/DELETE | `/api/phieu-thu` | Phiếu thu |

| GET/POST/PUT/DELETE | `/api/phieu-chi` | Phiếu chi |

### Sản phẩm

| Method | Endpoint | Mô tả |

|--------|----------|-------|

| GET/POST/PUT/DELETE | `/api/sanpham` | Sản phẩm |

| GET/POST/DELETE | `/api/sanpham/{id}/hinhanh` | Hình ảnh sản phẩm |

| GET/POST/PUT/DELETE | `/api/loaisanpham` | Loại sản phẩm |

### Nhân viên \& Hoạt động

| Method | Endpoint | Mô tả |

|--------|----------|-------|

| GET | `/api/nhan-vien` | Danh sách nhân viên |

| GET/POST/PUT/DELETE | `/api/hoat-dong` | Hoạt động chăm sóc |

---

## Troubleshooting

*\*Backend không start — lỗi kết nối DB\*\*

- Kiểm tra MySQL đang chạy: `mysql -u root -p`
- Kiểm tra đúng tên DB `CRMOnline` (phân biệt hoa/thường)
- Đảm bảo `username` và `password` trong `application.yml` đúng

*\*Lỗi compile `does not override abstract method`\*\*

- Chạy `mvn clean` rồi build lại
- Kiểm tra tất cả method trong interface domain đã được implement ở adapter tương ứng trong `crm-persistence`

*\*Frontend lỗi CORS\*\*

- Tất cả controller đã có `@CrossOrigin(origins = "\*")` nên không cần cấu hình thêm
- Nếu vẫn lỗi, kiểm tra URL trong `VITE\_API\_BASE\_URL` có đúng port không

*\*Frontend không thấy data\*\*

- Mở DevTools → Network, kiểm tra request có trả về 200 không
- Kiểm tra backend đang chạy tại đúng port (mặc định 8081)

---

## Build production

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

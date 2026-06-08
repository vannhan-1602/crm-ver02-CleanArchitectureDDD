# CRM Backend

## Kien truc hien tai

Repo nay dang di theo huong DDD ket hop CQRS muc nhe, voi cac lop chinh sau:

- `crm-api`: lop bootstrap va cau hinh Spring Boot
- `crm-presentation`: lop REST API, nhan request tu frontend
- `crm-application`: chua command/query va handler xu ly use case
- `crm-persistence`: chua JPA entity, Spring Data repository, va adapter map giua DB va domain
- `crm-domain`: chua `Entity`, `Value Object`, logic nghiep vu, va interface repository
- `crm-infrastructure`: hien chua co implementation thuc te, dang la module du phong

## Nhung gi project dang co that

- `Repository Pattern`: co
- `CQRS`: co o muc tach command/query va handler rieng
- `REST API`: co
- `gRPC`: chua co
- `MediatR`: chua dung, application hien dang xu ly bang cac handler thuong
- `DDD`: co o muc module hoa domain, entity, value object va repository interface

## Luong xu ly hien tai

1. `HopDongController` nhan HTTP request
2. Controller goi sang command handler hoac query handler trong `crm-application`
3. Handler lam viec qua `HopDongRepo` trong `crm-domain`
4. `HopDongPersistenceAdapter` trong `crm-persistence` chuyen doi giua domain model va JPA entity
5. JPA entity duoc map xuong bang `hd_hopdong`

## Ghi chu

- Bang hop dong trong database duoc map dung la `hd_hopdong`
- Hien tai project chua trien khai gRPC hoac pipeline kieu MediatR
- Neu muon bam sat mo ta kien truc ban dau hon nua, can bo sung them phan infrastructure that va mot co che mediator ro rang cho application layer

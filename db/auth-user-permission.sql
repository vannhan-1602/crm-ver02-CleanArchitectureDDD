CREATE TABLE IF NOT EXISTS CRM_UserAuth (
    User_Id INT NOT NULL,
    Username VARCHAR(100) NOT NULL,
    PasswordHash VARCHAR(255) NULL,
    RoleCode VARCHAR(30) NOT NULL DEFAULT 'sale',
    ChucVu VARCHAR(100) NULL,
    PhongBan VARCHAR(100) NULL,
    Active TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (User_Id),
    UNIQUE KEY UK_CRM_UserAuth_Username (Username)
);

CREATE TABLE IF NOT EXISTS CRM_UserPermission (
    User_Id INT NOT NULL,
    ModuleCode VARCHAR(50) NOT NULL,
    CanView TINYINT(1) NOT NULL DEFAULT 0,
    CanCreate TINYINT(1) NOT NULL DEFAULT 0,
    CanUpdate TINYINT(1) NOT NULL DEFAULT 0,
    CanDelete TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (User_Id, ModuleCode),
    CONSTRAINT FK_CRM_UserPermission_UserAuth
        FOREIGN KEY (User_Id) REFERENCES CRM_UserAuth(User_Id)
        ON DELETE CASCADE
);

-- Tai khoan admin mau: username admin / password admin123.
-- Script gan admin cho HT_User dau tien dang co trong database.
INSERT INTO CRM_UserAuth (User_Id, Username, PasswordHash, RoleCode, ChucVu, PhongBan, Active)
SELECT u.Id,
       'admin',
       '$2a$10$Qvzj7F9D7hcp1H.iXBTDzOh1c/P.s0Dd8c5gPIgDQ0n4Y9EWFNpc2',
       'admin',
       'Quan tri vien',
       'He thong',
       1
FROM HT_User u
ORDER BY u.Id
LIMIT 1
ON DUPLICATE KEY UPDATE
    RoleCode = VALUES(RoleCode),
    ChucVu = VALUES(ChucVu),
    PhongBan = VALUES(PhongBan),
    Active = VALUES(Active);

INSERT INTO CRM_UserPermission (User_Id, ModuleCode, CanView, CanCreate, CanUpdate, CanDelete)
SELECT a.User_Id, m.ModuleCode, 1, 1, 1, 1
FROM CRM_UserAuth a
JOIN (
    SELECT 'leads' AS ModuleCode UNION ALL
    SELECT 'khach-hang' UNION ALL
    SELECT 'hoat-dong' UNION ALL
    SELECT 'hop-dong' UNION ALL
    SELECT 'tai-chinh' UNION ALL
    SELECT 'sanpham' UNION ALL
    SELECT 'tickets' UNION ALL
    SELECT 'cohoi' UNION ALL
    SELECT 'baogia' UNION ALL
    SELECT 'bao-cao-thong-ke'
) m
WHERE a.Username = 'admin'
ON DUPLICATE KEY UPDATE
    CanView = VALUES(CanView),
    CanCreate = VALUES(CanCreate),
    CanUpdate = VALUES(CanUpdate),
    CanDelete = VALUES(CanDelete);

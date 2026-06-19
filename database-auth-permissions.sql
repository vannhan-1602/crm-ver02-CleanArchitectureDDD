CREATE TABLE IF NOT EXISTS `HT_UserModulePermission` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `User_Id` int unsigned NOT NULL,
  `ModuleKey` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `CanView` tinyint(1) NOT NULL DEFAULT 0,
  `CanRead` tinyint(1) NOT NULL DEFAULT 0,
  `CanWrite` tinyint(1) NOT NULL DEFAULT 0,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uk_user_module_permission` (`User_Id`, `ModuleKey`),
  CONSTRAINT `fk_user_module_permission_user`
    FOREIGN KEY (`User_Id`) REFERENCES `HT_User` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- Admin (Role_Id = 1) duoc bypass toan bo quyen trong code.
-- Gan quyen mau cho Sale user id = 3:
-- INSERT INTO `HT_UserModulePermission` (`User_Id`, `ModuleKey`, `CanView`, `CanRead`, `CanWrite`) VALUES
--   (3, 'LEAD', 1, 1, 1),
--   (3, 'KHACH_HANG', 1, 1, 0),
--   (3, 'CO_HOI', 1, 1, 1);

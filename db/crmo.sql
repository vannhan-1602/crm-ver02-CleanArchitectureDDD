-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: crmonline
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS `CRMOnline`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_bin;
USE `CRMOnline`;

--
-- Table structure for table `bh_cohoibanhang`
--

DROP TABLE IF EXISTS `bh_cohoibanhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bh_cohoibanhang` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `TenThuongVu` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `GiaiDoan` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'KhaoSat',
  `KhachHang_Id` bigint unsigned DEFAULT NULL,
  `Lead_Id` bigint unsigned DEFAULT NULL,
  `TyLeThanhCong` int DEFAULT '0',
  `DoanhThuKyVong` decimal(18,2) DEFAULT NULL,
  `GhiChu` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `NgayDuKien` date DEFAULT NULL,
  `NhanVienPhuTrach_Id` int unsigned DEFAULT NULL,
  `IsDeleted` tinyint(1) DEFAULT '0',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  KEY `fk_ch_kh` (`KhachHang_Id`),
  KEY `fk_ch_nv` (`NhanVienPhuTrach_Id`),
  KEY `fk_ch_lead` (`Lead_Id`),
  CONSTRAINT `fk_ch_kh` FOREIGN KEY (`KhachHang_Id`) REFERENCES `kh_khachhang` (`Id`),
  CONSTRAINT `fk_ch_lead` FOREIGN KEY (`Lead_Id`) REFERENCES `kh_lead` (`Id`) ON DELETE SET NULL,
  CONSTRAINT `fk_ch_nv` FOREIGN KEY (`NhanVienPhuTrach_Id`) REFERENCES `ht_user` (`Id`),
  CONSTRAINT `chk_ty_le` CHECK ((`TyLeThanhCong` between 0 and 100))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bh_cohoibanhang`
--

LOCK TABLES `bh_cohoibanhang` WRITE;
/*!40000 ALTER TABLE `bh_cohoibanhang` DISABLE KEYS */;
INSERT INTO `bh_cohoibanhang` VALUES (1,'Dự án triển khai CRM Pro cho DEF','ThanhCong',1,NULL,100,25000000.00,'Đã ký hợp đồng',NULL,2,0,'2026-05-02 11:00:45','2026-05-02 11:00:45'),(2,'Bán gói CRM Basic cho XYZ','ThuongLuong',2,NULL,60,5000000.00,'Khách đang xin duyệt budget',NULL,3,0,'2026-05-02 11:00:45','2026-05-02 11:00:45'),(3,'Mở rộng dịch vụ cho cửa hàng Hoa Lan','DeXuat',NULL,2,30,10000000.00,'Đã gửi file Proposal',NULL,3,0,'2026-05-02 11:00:45','2026-05-02 11:00:45');
/*!40000 ALTER TABLE `bh_cohoibanhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bh_loaisanpham`
--

DROP TABLE IF EXISTS `bh_loaisanpham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bh_loaisanpham` (
  `Id` int unsigned NOT NULL AUTO_INCREMENT,
  `TenLoai` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `MoTa` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bh_loaisanpham`
--

LOCK TABLES `bh_loaisanpham` WRITE;
/*!40000 ALTER TABLE `bh_loaisanpham` DISABLE KEYS */;
INSERT INTO `bh_loaisanpham` VALUES (1,'Phần mềm','Bản quyền PM'),(2,'Dịch vụ','Triển khai, bảo trì');
/*!40000 ALTER TABLE `bh_loaisanpham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bh_sanpham`
--

DROP TABLE IF EXISTS `bh_sanpham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bh_sanpham` (
  `Id` int unsigned NOT NULL AUTO_INCREMENT,
  `LoaiSanPham_Id` int unsigned DEFAULT NULL,
  `MaSP` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TenSP` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `DonVi` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `GiaBan` decimal(18,2) DEFAULT '0.00',
  `SoLuongTon` int DEFAULT '0',
  `TrangThai` tinyint DEFAULT '1',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `MaSP` (`MaSP`),
  KEY `fk_sp_loai` (`LoaiSanPham_Id`),
  CONSTRAINT `fk_sp_loai` FOREIGN KEY (`LoaiSanPham_Id`) REFERENCES `bh_loaisanpham` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bh_sanpham`
--

LOCK TABLES `bh_sanpham` WRITE;
/*!40000 ALTER TABLE `bh_sanpham` DISABLE KEYS */;
INSERT INTO `bh_sanpham` VALUES (1,1,'CRM-BASIC','Phần mềm CRM Bản Basic','License',5000000.00,50,1,'2026-05-02 11:00:44','2026-05-02 11:00:44'),(2,1,'CRM-PRO','Phần mềm CRM Bản Pro','License',15000000.00,30,1,'2026-05-02 11:00:44','2026-05-02 11:00:44'),(3,2,'SRV-SETUP','Dịch vụ Triển khai hệ thống','Gói',10000000.00,999,1,'2026-05-02 11:00:44','2026-05-02 11:00:44'),(4,1,'1','Bánh Bao','Cái',NULL,0,1,'2026-05-02 12:31:59','2026-05-02 12:31:59');
/*!40000 ALTER TABLE `bh_sanpham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bh_sanpham_hinhanh`
--

DROP TABLE IF EXISTS `bh_sanpham_hinhanh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bh_sanpham_hinhanh` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `SanPham_Id` int unsigned DEFAULT NULL,
  `UrlHinhAnh` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `IsMain` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_sphinhanh_sp` (`SanPham_Id`),
  CONSTRAINT `fk_sphinhanh_sp` FOREIGN KEY (`SanPham_Id`) REFERENCES `bh_sanpham` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bh_sanpham_hinhanh`
--

LOCK TABLES `bh_sanpham_hinhanh` WRITE;
/*!40000 ALTER TABLE `bh_sanpham_hinhanh` DISABLE KEYS */;
INSERT INTO `bh_sanpham_hinhanh` VALUES (6,1,'/uploads/products/af234411-2d78-4cfd-b36b-10d06dbaaf20_desktop-wallpaper-windows-10-black-and-white-r-s-windows-10-white (2).jpg',1);
/*!40000 ALTER TABLE `bh_sanpham_hinhanh` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hd_baogia`
--

DROP TABLE IF EXISTS `hd_baogia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hd_baogia` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `MaBaoGia` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `KhachHang_Id` bigint unsigned NOT NULL,
  `TongTien` decimal(18,2) DEFAULT '0.00',
  `TrangThai` enum('Nhap','DaGui','TuChoi','ChapNhan') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'Nhap',
  `NhanVien_Id` int unsigned DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `MaBaoGia` (`MaBaoGia`),
  KEY `fk_bg_kh` (`KhachHang_Id`),
  CONSTRAINT `fk_bg_kh` FOREIGN KEY (`KhachHang_Id`) REFERENCES `kh_khachhang` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hd_baogia`
--

LOCK TABLES `hd_baogia` WRITE;
/*!40000 ALTER TABLE `hd_baogia` DISABLE KEYS */;
INSERT INTO `hd_baogia` VALUES (1,'BG-2605-001',1,10000000.00,'Nhap',3,'2026-05-04 08:42:57','2026-05-04 08:42:57'),(2,'BG-2605-002',2,25000000.00,'DaGui',2,'2026-05-04 08:42:57','2026-05-04 08:42:57'),(3,'BG-2605-003',3,5000000.00,'ChapNhan',3,'2026-05-04 08:42:57','2026-05-04 08:42:57');
/*!40000 ALTER TABLE `hd_baogia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hd_baogia_chitiet`
--

DROP TABLE IF EXISTS `hd_baogia_chitiet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hd_baogia_chitiet` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `BaoGia_Id` bigint unsigned NOT NULL,
  `SanPham_Id` int unsigned NOT NULL,
  `SoLuong` int NOT NULL DEFAULT 0,
  `DonGia` decimal(18,2) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_bgct_bg` (`BaoGia_Id`),
  KEY `fk_bgct_sp` (`SanPham_Id`),
  CONSTRAINT `fk_bgct_bg` FOREIGN KEY (`BaoGia_Id`) REFERENCES `hd_baogia` (`Id`) ON DELETE CASCADE,
  CONSTRAINT `fk_bgct_sp` FOREIGN KEY (`SanPham_Id`) REFERENCES `bh_sanpham` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hd_baogia_chitiet`
--

LOCK TABLES `hd_baogia_chitiet` WRITE;
/*!40000 ALTER TABLE `hd_baogia_chitiet` DISABLE KEYS */;
INSERT INTO `hd_baogia_chitiet` VALUES (1,1,1,2,5000000.00),(2,2,2,1,15000000.00),(3,2,3,1,10000000.00),(4,3,1,1,5000000.00);
/*!40000 ALTER TABLE `hd_baogia_chitiet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hd_hopdong`
--

DROP TABLE IF EXISTS `hd_hopdong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hd_hopdong` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `MaHopDong` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `KhachHang_Id` bigint unsigned NOT NULL,
  `NgayKy` date DEFAULT NULL,
  `ThoiHan` int DEFAULT NULL,
  `TrangThai` enum('DangThucHien','TamDung','ThanhLy') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'DangThucHien',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `MaHopDong` (`MaHopDong`),
  KEY `fk_hdong_kh` (`KhachHang_Id`),
  CONSTRAINT `fk_hdong_kh` FOREIGN KEY (`KhachHang_Id`) REFERENCES `kh_khachhang` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hd_hopdong`
--

LOCK TABLES `hd_hopdong` WRITE;
/*!40000 ALTER TABLE `hd_hopdong` DISABLE KEYS */;
INSERT INTO `hd_hopdong` VALUES (1,'HD-DEF-2026',1,'2026-05-02',12,'DangThucHien','2026-05-02 11:00:45','2026-05-02 11:00:45'),(2,'HD002',4,'2026-05-15',3,'DangThucHien','2026-05-04 08:51:39','2026-05-04 08:51:39');
/*!40000 ALTER TABLE `hd_hopdong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ht_chucvu`
--

DROP TABLE IF EXISTS `ht_chucvu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ht_chucvu` (
  `Id` smallint unsigned NOT NULL AUTO_INCREMENT,
  `TenChucVu` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `IsActive` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `TenChucVu` (`TenChucVu`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ht_chucvu`
--

LOCK TABLES `ht_chucvu` WRITE;
/*!40000 ALTER TABLE `ht_chucvu` DISABLE KEYS */;
INSERT INTO `ht_chucvu` VALUES (1,'Giám Đốc',1),(2,'Trưởng Phòng',1),(3,'Nhân Viên',1);
/*!40000 ALTER TABLE `ht_chucvu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ht_phongban`
--

DROP TABLE IF EXISTS `ht_phongban`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ht_phongban` (
  `Id` smallint unsigned NOT NULL AUTO_INCREMENT,
  `TenPhongBan` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `IsActive` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `TenPhongBan` (`TenPhongBan`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ht_phongban`
--

LOCK TABLES `ht_phongban` WRITE;
/*!40000 ALTER TABLE `ht_phongban` DISABLE KEYS */;
INSERT INTO `ht_phongban` VALUES (1,'Ban Giám Đốc',1),(2,'Phòng Kinh Doanh',1),(3,'Phòng Kế Toán',1),(4,'Phòng Kỹ Thuật',1);
/*!40000 ALTER TABLE `ht_phongban` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ht_role`
--

DROP TABLE IF EXISTS `ht_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ht_role` (
  `Id` int unsigned NOT NULL AUTO_INCREMENT,
  `TenRole` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `MoTa` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ht_role`
--

LOCK TABLES `ht_role` WRITE;
/*!40000 ALTER TABLE `ht_role` DISABLE KEYS */;
INSERT INTO `ht_role` VALUES (1,'Admin','Quản trị viên hệ thống'),(2,'Manager','Quản lý kinh doanh'),(3,'Sale','Nhân viên kinh doanh'),(4,'Accountant','Nhân viên kế toán');
/*!40000 ALTER TABLE `ht_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ht_thongtinnhansu`
--

DROP TABLE IF EXISTS `ht_thongtinnhansu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ht_thongtinnhansu` (
  `Id` int unsigned NOT NULL AUTO_INCREMENT,
  `HoTen` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `Email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `SoDienThoai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `PhongBan_Id` smallint unsigned DEFAULT NULL,
  `ChucVu_Id` smallint unsigned DEFAULT NULL,
  `TrangThai` tinyint(1) DEFAULT '1',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Email` (`Email`),
  KEY `fk_ns_phongban` (`PhongBan_Id`),
  KEY `fk_ns_chucvu` (`ChucVu_Id`),
  CONSTRAINT `fk_ns_chucvu` FOREIGN KEY (`ChucVu_Id`) REFERENCES `ht_chucvu` (`Id`),
  CONSTRAINT `fk_ns_phongban` FOREIGN KEY (`PhongBan_Id`) REFERENCES `ht_phongban` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ht_thongtinnhansu`
--

LOCK TABLES `ht_thongtinnhansu` WRITE;
/*!40000 ALTER TABLE `ht_thongtinnhansu` DISABLE KEYS */;
INSERT INTO `ht_thongtinnhansu` VALUES (1,'Phạm Nguyễn Quốc Long','long.admin@crm.vn','0901234567',1,1,1,'2026-05-02 11:00:44','2026-05-02 11:00:44'),(2,'Trần Thu Hà','ha.manager@crm.vn','0987654321',2,2,1,'2026-05-02 11:00:44','2026-05-02 11:00:44'),(3,'Lê Kinh Doanh','kinhdoanh1@crm.vn','0911222333',2,3,1,'2026-05-02 11:00:44','2026-05-02 11:00:44'),(4,'Nguyễn Thị Kế Toán','ketoan1@crm.vn','0944555666',3,3,1,'2026-05-02 11:00:44','2026-05-02 11:00:44');
/*!40000 ALTER TABLE `ht_thongtinnhansu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ht_user`
--

DROP TABLE IF EXISTS `ht_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ht_user` (
  `Id` int unsigned NOT NULL AUTO_INCREMENT,
  `NhanSu_Id` int unsigned DEFAULT NULL,
  `Username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `Password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `Role_Id` int unsigned DEFAULT NULL,
  `TrangThai` enum('Active','Locked','Inactive') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'Active',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Username` (`Username`),
  UNIQUE KEY `NhanSu_Id` (`NhanSu_Id`),
  KEY `fk_user_role` (`Role_Id`),
  CONSTRAINT `fk_user_nhansu` FOREIGN KEY (`NhanSu_Id`) REFERENCES `ht_thongtinnhansu` (`Id`) ON DELETE SET NULL,
  CONSTRAINT `fk_user_role` FOREIGN KEY (`Role_Id`) REFERENCES `ht_role` (`Id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ht_user`
--

LOCK TABLES `ht_user` WRITE;
/*!40000 ALTER TABLE `ht_user` DISABLE KEYS */;
INSERT INTO `ht_user` VALUES (1,1,'admin','123456',1,'Active','2026-05-02 11:00:44','2026-05-02 11:00:44'),(2,2,'manager','123456',2,'Active','2026-05-02 11:00:44','2026-05-02 11:00:44'),(3,3,'sale01','123456',3,'Active','2026-05-02 11:00:44','2026-05-02 11:00:44'),(4,4,'ketoan01','123456',4,'Active','2026-05-02 11:00:44','2026-05-02 11:00:44');
/*!40000 ALTER TABLE `ht_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ht_usermodulepermission`
--

DROP TABLE IF EXISTS `ht_usermodulepermission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ht_usermodulepermission` (
  `Id` bigint NOT NULL AUTO_INCREMENT,
  `User_Id` int unsigned NOT NULL,
  `ModuleKey` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `CanView` tinyint(1) NOT NULL DEFAULT '0',
  `CanRead` tinyint(1) NOT NULL DEFAULT '0',
  `CanWrite` tinyint(1) NOT NULL DEFAULT '0',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UK_HT_UserModulePermission_User_Module` (`User_Id`,`ModuleKey`),
  CONSTRAINT `FK_HT_UserModulePermission_User` FOREIGN KEY (`User_Id`) REFERENCES `ht_user` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ht_usermodulepermission`
--

LOCK TABLES `ht_usermodulepermission` WRITE;
/*!40000 ALTER TABLE `ht_usermodulepermission` DISABLE KEYS */;
INSERT INTO `ht_usermodulepermission` VALUES (1,1,'LEAD',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00'),(2,1,'KHACH_HANG',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00'),(3,1,'CO_HOI',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00'),(4,1,'HOAT_DONG',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00'),(5,1,'BAO_GIA',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00'),(6,1,'HOP_DONG',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00'),(7,1,'HOA_DON',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00'),(8,1,'TAI_CHINH',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00'),(9,1,'BAO_CAO',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00'),(10,1,'SAN_PHAM',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00'),(11,1,'TICKET',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00'),(12,1,'NHAN_VIEN',1,1,1,'2026-06-20 00:00:00','2026-06-20 00:00:00');
/*!40000 ALTER TABLE `ht_usermodulepermission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kh_diachi`
--

DROP TABLE IF EXISTS `kh_diachi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kh_diachi` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `KhachHang_Id` bigint unsigned NOT NULL,
  `DiaChiChiTiet` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `TinhThanh` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `QuanHuyen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `PhuongXa` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `LoaiDiaChi` enum('Billing','Shipping','Office') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `IsDefault` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`Id`),
  KEY `fk_dc_kh` (`KhachHang_Id`),
  CONSTRAINT `fk_dc_kh` FOREIGN KEY (`KhachHang_Id`) REFERENCES `kh_khachhang` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kh_diachi`
--

LOCK TABLES `kh_diachi` WRITE;
/*!40000 ALTER TABLE `kh_diachi` DISABLE KEYS */;
INSERT INTO `kh_diachi` VALUES (1,1,'Tòa nhà Landmark 81','Hồ Chí Minh','Quận 1','Bến Nghé','Office',1),(2,2,'Tòa nhà Etown','Hồ Chí Minh','Quận 10','Phường 11','Billing',1);
/*!40000 ALTER TABLE `kh_diachi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kh_hoatdong`
--

DROP TABLE IF EXISTS `kh_hoatdong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kh_hoatdong` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `KhachHang_Id` bigint unsigned DEFAULT NULL,
  `Lead_Id` bigint unsigned DEFAULT NULL,
  `LoaiHoatDong` enum('Call','Meeting','Email','Zalo') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `NoiDung` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ThoiGianThucHien` datetime DEFAULT NULL,
  `NhanVien_Id` int unsigned DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  KEY `fk_hd_kh` (`KhachHang_Id`),
  KEY `fk_hd_lead` (`Lead_Id`),
  KEY `fk_hd_nv` (`NhanVien_Id`),
  CONSTRAINT `fk_hd_kh` FOREIGN KEY (`KhachHang_Id`) REFERENCES `kh_khachhang` (`Id`) ON DELETE CASCADE,
  CONSTRAINT `fk_hd_lead` FOREIGN KEY (`Lead_Id`) REFERENCES `kh_lead` (`Id`) ON DELETE CASCADE,
  CONSTRAINT `fk_hd_nv` FOREIGN KEY (`NhanVien_Id`) REFERENCES `ht_user` (`Id`),
  CONSTRAINT `chk_hd_target` CHECK (((`KhachHang_Id` is not null) or (`Lead_Id` is not null)))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kh_hoatdong`
--

LOCK TABLES `kh_hoatdong` WRITE;
/*!40000 ALTER TABLE `kh_hoatdong` DISABLE KEYS */;
INSERT INTO `kh_hoatdong` VALUES (2,NULL,2,'Zalo','Gửi báo giá sơ bộ qua Zalo.','2026-05-02 14:15:00',3,'2026-05-02 11:00:45','2026-05-02 11:00:45'),(3,1,NULL,'Email','Gặp mặt ký hợp đồng triển khai CRM cho doanh nghiệp','2026-05-02 10:00:00',2,'2026-05-02 11:00:45','2026-05-08 10:04:32'),(4,4,NULL,'Meeting','chào','2026-05-14 06:53:00',3,'2026-05-04 08:51:10','2026-05-04 08:51:10'),(5,1,2,'Meeting','Gặp nhậu','2026-05-07 13:10:00',3,NULL,NULL);
/*!40000 ALTER TABLE `kh_hoatdong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kh_khachhang`
--

DROP TABLE IF EXISTS `kh_khachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kh_khachhang` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `MaKhachHang` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TenKhachHang` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `LoaiKhachHang_Id` smallint unsigned DEFAULT NULL,
  `TinhTrang_Id` smallint unsigned DEFAULT NULL,
  `Email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `SoDienThoai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `MaSoThue` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `NhanVienPhuTrach_Id` int unsigned DEFAULT NULL,
  `IsDeleted` tinyint(1) DEFAULT '0',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `MaKhachHang` (`MaKhachHang`),
  KEY `idx_kh_sdt` (`SoDienThoai`),
  KEY `idx_kh_filter` (`NhanVienPhuTrach_Id`,`IsDeleted`,`TinhTrang_Id`),
  KEY `fk_kh_loai` (`LoaiKhachHang_Id`),
  KEY `fk_kh_ttrang` (`TinhTrang_Id`),
  FULLTEXT KEY `idx_fts_kh` (`TenKhachHang`,`Email`),
  CONSTRAINT `fk_kh_loai` FOREIGN KEY (`LoaiKhachHang_Id`) REFERENCES `kh_loaikhachhang` (`Id`),
  CONSTRAINT `fk_kh_nv` FOREIGN KEY (`NhanVienPhuTrach_Id`) REFERENCES `ht_user` (`Id`),
  CONSTRAINT `fk_kh_ttrang` FOREIGN KEY (`TinhTrang_Id`) REFERENCES `kh_tinhtrangkhachhang` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kh_khachhang`
--

LOCK TABLES `kh_khachhang` WRITE;
/*!40000 ALTER TABLE `kh_khachhang` DISABLE KEYS */;
INSERT INTO `kh_khachhang` VALUES (1,'KH0001','Tập đoàn DEF (Đã Convert)',1,1,'hung.ceo@def.vn','0988777666','0311223344',1,0,'2026-05-02 11:00:45','2026-05-02 11:25:00'),(2,'KH0002','Công ty TNHH XYZ',2,1,'contact@xyz.com','0283888999','0102030405',3,0,'2026-05-02 11:00:45','2026-05-02 11:00:45'),(3,'KH0003','Nguyễn Văn Minh',3,2,'minhnv@yahoo.com','0912345678',NULL,3,0,'2026-05-02 11:00:45','2026-05-02 11:00:45'),(4,'KH004','Nhân - Công ty ABC',NULL,NULL,'a@gmail.com','0933333333',NULL,NULL,0,'2026-05-04 08:49:40','2026-05-06 03:05:58'),(5,'KH0005','Công ty Test ABC',2,1,'test@abc.com','0901234567','0123456789',3,0,'2026-06-07 03:10:11','2026-06-07 03:10:11');
/*!40000 ALTER TABLE `kh_khachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kh_lead`
--

DROP TABLE IF EXISTS `kh_lead`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kh_lead` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `TenLead` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TenCongTy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `SoDienThoai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `Email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `TinhTrang` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `NhanVienPhuTrach_Id` int unsigned DEFAULT NULL,
  `IsDeleted` tinyint(1) NOT NULL DEFAULT '0',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  KEY `fk_lead_nv` (`NhanVienPhuTrach_Id`),
  FULLTEXT KEY `idx_fts_lead` (`TenLead`,`TenCongTy`),
  CONSTRAINT `fk_lead_nv` FOREIGN KEY (`NhanVienPhuTrach_Id`) REFERENCES `ht_user` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kh_lead`
--

LOCK TABLES `kh_lead` WRITE;
/*!40000 ALTER TABLE `kh_lead` DISABLE KEYS */;
INSERT INTO `kh_lead` VALUES (1,'Anh Tuấn','Công ty ABC','0901112222','tuan@abc.com','Moi',3,0,'2026-05-02 11:00:45','2026-06-09 09:54:48'),(2,'Chị Lan','Cửa hàng Hoa Lan','0933444555','lan.hoa@gmail.com','DangChamSoc',3,0,'2026-05-02 11:00:45','2026-06-09 09:54:48'),(3,'Giám đốc Hùng','Tập đoàn DEF','0988777666','hung.ceo@def.vn','DaChuyenDoi',2,0,'2026-05-02 11:00:45','2026-06-09 09:54:48'),(4,'Nhân','Công ty ABCCCCC','0933333333','a@gmail.com','DaChuyenDoi',2,0,'2026-05-04 08:49:36','2026-06-09 09:54:48');
/*!40000 ALTER TABLE `kh_lead` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kh_loaikhachhang`
--

DROP TABLE IF EXISTS `kh_loaikhachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kh_loaikhachhang` (
  `Id` smallint unsigned NOT NULL AUTO_INCREMENT,
  `TenLoai` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `MoTa` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `IsActive` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kh_loaikhachhang`
--

LOCK TABLES `kh_loaikhachhang` WRITE;
/*!40000 ALTER TABLE `kh_loaikhachhang` DISABLE KEYS */;
INSERT INTO `kh_loaikhachhang` VALUES (1,'VIP','Khách hàng chiến lược',1),(2,'B2B','Doanh nghiệp',1),(3,'B2C','Cá nhân',1);
/*!40000 ALTER TABLE `kh_loaikhachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kh_tinhtrangkhachhang`
--

DROP TABLE IF EXISTS `kh_tinhtrangkhachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kh_tinhtrangkhachhang` (
  `Id` smallint unsigned NOT NULL AUTO_INCREMENT,
  `TenTinhTrang` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `IsActive` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kh_tinhtrangkhachhang`
--

LOCK TABLES `kh_tinhtrangkhachhang` WRITE;
/*!40000 ALTER TABLE `kh_tinhtrangkhachhang` DISABLE KEYS */;
INSERT INTO `kh_tinhtrangkhachhang` VALUES (1,'Đang giao dịch',1),(2,'Tiềm năng',1),(3,'Ngừng giao dịch',1);
/*!40000 ALTER TABLE `kh_tinhtrangkhachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kho_thekho`
--

DROP TABLE IF EXISTS `kho_thekho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kho_thekho` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `SanPham_Id` int unsigned NOT NULL,
  `MaChungTu` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `LoaiGiaoDich` enum('NhapMua','XuatBan','NhapTraKhach','XuatTraNCC','XuatHuy','KiemKe') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `SoLuongThayDoi` int NOT NULL COMMENT 'Dấu cộng (+) là Nhập, Dấu trừ (-) là Xuất',
  `TonCuoi` int NOT NULL COMMENT 'Số lượng tồn lũy kế ngay sau khi giao dịch này xảy ra',
  `NgayGiaoDich` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `NguoiThucHien_Id` int unsigned DEFAULT NULL,
  `GhiChu` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_thekho_user` (`NguoiThucHien_Id`),
  KEY `idx_thekho_truyvan` (`SanPham_Id`,`NgayGiaoDich`),
  CONSTRAINT `fk_thekho_sp` FOREIGN KEY (`SanPham_Id`) REFERENCES `bh_sanpham` (`Id`),
  CONSTRAINT `fk_thekho_user` FOREIGN KEY (`NguoiThucHien_Id`) REFERENCES `ht_user` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kho_thekho`
--

LOCK TABLES `kho_thekho` WRITE;
/*!40000 ALTER TABLE `kho_thekho` DISABLE KEYS */;
INSERT INTO `kho_thekho` VALUES (1,1,'NK-001','NhapMua',50,50,'2026-05-02 11:00:45',1,'Nhập license CRM Basic đầu kỳ'),(2,2,'NK-001','NhapMua',31,31,'2026-05-02 11:00:45',1,'Nhập license CRM Pro đầu kỳ'),(3,2,'PX-001','XuatBan',1,30,'2026-05-02 11:00:45',2,'Xuất license cho hợp đồng HD-DEF-2026');
/*!40000 ALTER TABLE `kho_thekho` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kt_hoadon`
--

DROP TABLE IF EXISTS `kt_hoadon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kt_hoadon` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `MaHoaDon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `HopDong_Id` bigint unsigned DEFAULT NULL,
  `KhachHang_Id` bigint unsigned NOT NULL,
  `TongTien` decimal(18,2) NOT NULL,
  `SoTienDaThu` decimal(18,2) DEFAULT '0.00',
  `TrangThaiThanhToan` enum('ChuaThanhToan','ThanhToan1Phan','HoanTat') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'ChuaThanhToan',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `MaHoaDon` (`MaHoaDon`),
  KEY `fk_hdon_kh` (`KhachHang_Id`),
  KEY `fk_hdon_hopdong` (`HopDong_Id`),
  CONSTRAINT `fk_hdon_hopdong` FOREIGN KEY (`HopDong_Id`) REFERENCES `hd_hopdong` (`Id`) ON DELETE SET NULL,
  CONSTRAINT `fk_hdon_kh` FOREIGN KEY (`KhachHang_Id`) REFERENCES `kh_khachhang` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kt_hoadon`
--

LOCK TABLES `kt_hoadon` WRITE;
/*!40000 ALTER TABLE `kt_hoadon` DISABLE KEYS */;
INSERT INTO `kt_hoadon` VALUES (1,'INV-2605-001',1,1,25000000.00,10000000.00,'ThanhToan1Phan','2026-05-02 11:00:45','2026-05-02 11:00:45'),(3,'INV-2605-002',2,2,20000000.00,1000000.00,'ThanhToan1Phan','2026-06-09 21:35:47','2026-06-09 21:37:05');
/*!40000 ALTER TABLE `kt_hoadon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kt_phieuthuchi`
--

DROP TABLE IF EXISTS `kt_phieuthuchi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kt_phieuthuchi` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `MaPhieu` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `LoaiPhieu` enum('Thu','Chi') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `KhachHang_Id` bigint unsigned DEFAULT NULL,
  `HoaDon_Id` bigint unsigned DEFAULT NULL,
  `SoTien` decimal(18,2) NOT NULL,
  `NguoiLap_Id` int unsigned DEFAULT NULL,
  `NgayTao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `MaPhieu` (`MaPhieu`),
  KEY `fk_ptc_kh` (`KhachHang_Id`),
  KEY `fk_ptc_hdon` (`HoaDon_Id`),
  KEY `fk_ptc_user` (`NguoiLap_Id`),
  CONSTRAINT `fk_ptc_hdon` FOREIGN KEY (`HoaDon_Id`) REFERENCES `kt_hoadon` (`Id`),
  CONSTRAINT `fk_ptc_kh` FOREIGN KEY (`KhachHang_Id`) REFERENCES `kh_khachhang` (`Id`),
  CONSTRAINT `fk_ptc_user` FOREIGN KEY (`NguoiLap_Id`) REFERENCES `ht_user` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kt_phieuthuchi`
--

LOCK TABLES `kt_phieuthuchi` WRITE;
/*!40000 ALTER TABLE `kt_phieuthuchi` DISABLE KEYS */;
INSERT INTO `kt_phieuthuchi` VALUES (1,'PT-0001','Thu',1,1,10000000.00,4,'2026-05-02 11:00:45','2026-05-02 11:00:45'),(7,'PT-0002','Thu',2,3,1000000.00,4,'2026-06-09 21:37:05','2026-06-09 21:37:05');
/*!40000 ALTER TABLE `kt_phieuthuchi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_auditlog`
--

DROP TABLE IF EXISTS `sys_auditlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_auditlog` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `TableName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `RecordId` bigint unsigned NOT NULL,
  `Action` enum('INSERT','UPDATE','DELETE') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `OldData` json DEFAULT NULL,
  `NewData` json DEFAULT NULL,
  `UserId` int unsigned DEFAULT NULL,
  `ChangedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  KEY `idx_audit_main` (`TableName`,`RecordId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_auditlog`
--

LOCK TABLES `sys_auditlog` WRITE;
/*!40000 ALTER TABLE `sys_auditlog` DISABLE KEYS */;
INSERT INTO `sys_auditlog` VALUES (1,'KH_KhachHang',5,'INSERT',NULL,'{\"Id\": 5, \"Email\": \"test@abc.com\", \"MaSoThue\": \"0123456789\", \"CreatedAt\": \"2026-06-07T03:10:11\", \"UpdatedAt\": \"2026-06-07T03:10:11\", \"MaKhachHang\": \"KH0005\", \"SoDienThoai\": \"0901234567\", \"TinhTrangId\": 1, \"TenKhachHang\": \"Công ty Test ABC\", \"LoaiKhachHangId\": 2, \"NhanVienPhuTrachId\": 3}',1,'2026-06-07 03:10:11');
/*!40000 ALTER TABLE `sys_auditlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tk_loaiticket`
--

DROP TABLE IF EXISTS `tk_loaiticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tk_loaiticket` (
  `Id` smallint unsigned NOT NULL AUTO_INCREMENT,
  `TenLoai` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'Tên loại ticket (Bảo hành, Khiếu nại, Hỗ trợ KT…)',
  `MoTa` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `IsActive` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uq_loai_ticket_ten` (`TenLoai`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tk_loaiticket`
--

LOCK TABLES `tk_loaiticket` WRITE;
/*!40000 ALTER TABLE `tk_loaiticket` DISABLE KEYS */;
INSERT INTO `tk_loaiticket` VALUES (1,'Bảo hành','Yêu cầu bảo hành sản phẩm / dịch vụ',1),(2,'Khiếu nại','Khiếu nại chất lượng hoặc dịch vụ',1),(3,'Hỗ trợ kỹ thuật','Hỗ trợ cài đặt, lỗi kỹ thuật, hướng dẫn sử dụng',1);
/*!40000 ALTER TABLE `tk_loaiticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tk_ticket`
--

DROP TABLE IF EXISTS `tk_ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tk_ticket` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `MaTicket` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'Mã Ticket tự sinh',
  `TieuDe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `MoTa` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `FileDinhKem` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `LoaiTicket_Id` smallint unsigned DEFAULT NULL,
  `KhachHang_Id` bigint unsigned NOT NULL,
  `HopDong_Id` bigint unsigned DEFAULT NULL,
  `SanPham_Id` int unsigned DEFAULT NULL,
  `MucDoUuTien` enum('Thap','TrungBinh','Cao','KhanCap') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'TrungBinh',
  `NguonTiepNhan` enum('Email','Phone','Web','Zalo','TrucTiep') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'Phone',
  `TrangThai` enum('Moi','DangXuLy','ChoPhanHoi','Dong') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'Moi',
  `NhanVienTiepNhan_Id` int unsigned DEFAULT NULL,
  `NhanVienXuLy_Id` int unsigned DEFAULT NULL,
  `NgayHenXuLy` datetime DEFAULT NULL,
  `NgayDong` datetime DEFAULT NULL,
  `LyDoDong` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `IsDeleted` tinyint(1) DEFAULT '0',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uq_ticket_ma` (`MaTicket`),
  KEY `idx_ticket_trangthai` (`TrangThai`,`IsDeleted`),
  KEY `idx_ticket_uutien` (`MucDoUuTien`),
  KEY `idx_ticket_kh` (`KhachHang_Id`),
  KEY `idx_ticket_xuly` (`NhanVienXuLy_Id`),
  KEY `fk_ticket_loai` (`LoaiTicket_Id`),
  KEY `fk_ticket_hd` (`HopDong_Id`),
  KEY `fk_ticket_sp` (`SanPham_Id`),
  KEY `fk_ticket_tiepnhan` (`NhanVienTiepNhan_Id`),
  CONSTRAINT `fk_ticket_hd` FOREIGN KEY (`HopDong_Id`) REFERENCES `hd_hopdong` (`Id`) ON DELETE SET NULL,
  CONSTRAINT `fk_ticket_kh` FOREIGN KEY (`KhachHang_Id`) REFERENCES `kh_khachhang` (`Id`),
  CONSTRAINT `fk_ticket_loai` FOREIGN KEY (`LoaiTicket_Id`) REFERENCES `tk_loaiticket` (`Id`) ON DELETE SET NULL,
  CONSTRAINT `fk_ticket_sp` FOREIGN KEY (`SanPham_Id`) REFERENCES `bh_sanpham` (`Id`) ON DELETE SET NULL,
  CONSTRAINT `fk_ticket_tiepnhan` FOREIGN KEY (`NhanVienTiepNhan_Id`) REFERENCES `ht_user` (`Id`) ON DELETE SET NULL,
  CONSTRAINT `fk_ticket_xuly` FOREIGN KEY (`NhanVienXuLy_Id`) REFERENCES `ht_user` (`Id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tk_ticket`
--

LOCK TABLES `tk_ticket` WRITE;
/*!40000 ALTER TABLE `tk_ticket` DISABLE KEYS */;
INSERT INTO `tk_ticket` VALUES (1,'TK-2605-0001','Lỗi đăng nhập phần mềm CRM Pro sau khi nâng cấp','Sau khi nâng cấp lên phiên bản 3.2, tài khoản admin không thể đăng nhập, báo lỗi 403.',NULL,3,1,1,2,'Cao','Phone','DangXuLy',3,4,'2026-05-04 17:00:00',NULL,NULL,0,'2026-05-02 01:30:00','2026-05-02 02:15:00'),(2,'TK-2605-0002','Yêu cầu bảo hành license CRM – key bị vô hiệu hoá','License key mua tháng 3/2026 bị báo hết hạn trước thời hạn. Đề nghị cấp lại.',NULL,1,1,1,2,'TrungBinh','Email','Moi',3,NULL,'2026-05-06 17:00:00',NULL,NULL,0,'2026-05-02 03:05:00','2026-05-02 03:05:00'),(3,'TK-2605-0003','Khiếu nại hóa đơn bị tính sai dịch vụ triển khai','Hóa đơn INV-2605-001 tính thêm phí setup không có trong hợp đồng. Đề nghị xem xét điều chỉnh.',NULL,2,1,1,3,'KhanCap','Zalo','Dong',3,2,'2026-05-03 12:00:00','2026-05-02 15:45:00','Đã kiểm tra & xác nhận sai sót, xuất hóa đơn điều chỉnh.',0,'2026-05-02 02:00:00','2026-05-02 08:45:00');
/*!40000 ALTER TABLE `tk_ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tk_ticket_phanhoi`
--

DROP TABLE IF EXISTS `tk_ticket_phanhoi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tk_ticket_phanhoi` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `Ticket_Id` bigint unsigned NOT NULL,
  `NguoiPhanHoi_Id` int unsigned DEFAULT NULL,
  `LoaiPhanHoi` enum('NoiBoXuLy','PhanHoiKhachHang','YeuCauBoSung','DongTicket') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `NoiDung` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `FileDinhKem` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `TrangThaiTruoc` enum('Moi','DangXuLy','ChoPhanHoi','Dong') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `TrangThaiSau` enum('Moi','DangXuLy','ChoPhanHoi','Dong') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  KEY `idx_phanHoi_ticket` (`Ticket_Id`),
  KEY `idx_phanHoi_nguoi` (`NguoiPhanHoi_Id`),
  CONSTRAINT `fk_ph_ticket` FOREIGN KEY (`Ticket_Id`) REFERENCES `tk_ticket` (`Id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ph_user` FOREIGN KEY (`NguoiPhanHoi_Id`) REFERENCES `ht_user` (`Id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tk_ticket_phanhoi`
--

LOCK TABLES `tk_ticket_phanhoi` WRITE;
/*!40000 ALTER TABLE `tk_ticket_phanhoi` DISABLE KEYS */;
INSERT INTO `tk_ticket_phanhoi` VALUES (1,1,4,'NoiBoXuLy','Đã kiểm tra log server, phát hiện lỗi JWT expired sau khi nâng cấp. Đang rollback patch.',NULL,'Moi','DangXuLy','2026-05-02 02:15:00'),(2,1,4,'YeuCauBoSung','Vui lòng cung cấp thêm file log client-side (F12 → Console) để xác định chính xác nguyên nhân.',NULL,'DangXuLy','ChoPhanHoi','2026-05-02 04:00:00'),(3,3,2,'NoiBoXuLy','Đã đối chiếu hợp đồng HD-DEF-2026, xác nhận phí setup không có trong điều khoản. Sẽ xuất hóa đơn điều chỉnh.',NULL,'Moi','DangXuLy','2026-05-02 03:30:00'),(4,3,2,'DongTicket','Đã phát hành hóa đơn điều chỉnh INVADJ-001 giảm trừ phí setup. Khách hàng xác nhận đồng ý. Đóng Ticket.',NULL,'DangXuLy','Dong','2026-05-02 08:45:00');
/*!40000 ALTER TABLE `tk_ticket_phanhoi` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-20 19:13:04

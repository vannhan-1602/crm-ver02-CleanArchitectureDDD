package com.crm.persistence.jpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Table(name = "BH_CoHoiBanHang")
public class CoHoiBanHangJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private  Integer Id;
    @Column(name = "TenThuongVu")
    private String TenThuongVu;
    @Column(name = "GiaiDoan")
    private  String GiaiDoan;
    @Column(name = "KhachHang_Id")
    private Integer KhachHang_Id;
    @Column(name = "Lead_Id")
    private Integer Lead_Id;
    @Column(name = "TyLeThanhCong")
    private  int TyLeThanhCong;
    @Column(name = "DoanhThuKyVong")
    private double  DoanhThuKyVong;
    @Column(name = "GhiChu")
    private String GhiChu;
    @Column(name = "NgayDuKien")
    private Date NgayDuKien;
    @Column(name = "NhanVienPhuTrach_Id")
    private Integer NhanVienPhuTrach_Id;
    @Column(name = "IsDeleted")
    private int isDeleted=0;
    @Column(name = "CreatedAt")
    private LocalDateTime CreatedAt;
    @Column(name = "UpdatedAt")
    private LocalDateTime UpdatedAt;

    public CoHoiBanHangJpaEntity() {
    }

    public CoHoiBanHangJpaEntity(Integer id, String tenThuongVu, String giaiDoan, Integer khachHang_Id, Integer lead_Id, int tyLeThanhCong, double doanhThuKyVong, String ghiChu, Date ngayDuKien, Integer nhanVienPhuTrach_Id, int isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        Id = id;
        TenThuongVu = tenThuongVu;
        GiaiDoan = giaiDoan;
        KhachHang_Id = khachHang_Id;
        Lead_Id = lead_Id;
        TyLeThanhCong = tyLeThanhCong;
        DoanhThuKyVong = doanhThuKyVong;
        GhiChu = ghiChu;
        NgayDuKien = ngayDuKien;
        NhanVienPhuTrach_Id = nhanVienPhuTrach_Id;
        isDeleted = isDeleted;
        CreatedAt = createdAt;
        UpdatedAt = updatedAt;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getTenThuongVu() {
        return TenThuongVu;
    }

    public void setTenThuongVu(String tenThuongVu) {
        TenThuongVu = tenThuongVu;
    }

    public String getGiaiDoan() {
        return GiaiDoan;
    }

    public void setGiaiDoan(String giaiDoan) {
        GiaiDoan = giaiDoan;
    }

    public Integer getKhachHang_Id() {
        return KhachHang_Id;
    }

    public void setKhachHang_Id(Integer khachHang_Id) {
        KhachHang_Id = khachHang_Id;
    }

    public Integer getLead_Id() {
        return Lead_Id;
    }

    public void setLead_Id(Integer lead_Id) {
        Lead_Id = lead_Id;
    }

    public int getTyLeThanhCong() {
        return TyLeThanhCong;
    }

    public void setTyLeThanhCong(int tyLeThanhCong) {
        TyLeThanhCong = tyLeThanhCong;
    }

    public double getDoanhThuKyVong() {
        return DoanhThuKyVong;
    }

    public void setDoanhThuKyVong(double doanhThuKyVong) {
        DoanhThuKyVong = doanhThuKyVong;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }

    public Date getNgayDuKien() {
        return NgayDuKien;
    }

    public void setNgayDuKien(Date ngayDuKien) {
        NgayDuKien = ngayDuKien;
    }

    public Integer getNhanVienPhuTrach_Id() {
        return NhanVienPhuTrach_Id;
    }

    public void setNhanVienPhuTrach_Id(Integer nhanVienPhuTrach_Id) {
        NhanVienPhuTrach_Id = nhanVienPhuTrach_Id;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        CreatedAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        UpdatedAt = updatedAt;
    }
}

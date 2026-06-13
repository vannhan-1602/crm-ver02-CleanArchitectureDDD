package com.crm.domain.entities;

import java.time.LocalDateTime;
import java.util.Date;

public class CoHoiBanHang {
    private  Integer Id;
    private String TenThuongVu;
    private  String GiaiDoan;
    private Integer KhachHang_Id;
    private Integer Lead_Id;
    private  int TyLeThanhCong;
    private double  DoanhThuKyVong;
    private String GhiChu;
    private Date NgayDuKien;
    private Integer NhanVienPhuTrach_Id;
    private int IsDeleted;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;

    public CoHoiBanHang(String tenThuongVu, String giaiDoan, Integer khachHangId,
                        Integer leadId, int tyLeThanhCong, double doanhThuKyVong,
                        String ghiChu, Date ngayDuKien, Integer nhanVienPhuTrachId) {
        this.TenThuongVu        = tenThuongVu;      // ✅ thêm this.
        this.GiaiDoan           = giaiDoan;
        this.KhachHang_Id       = khachHangId;
        this.Lead_Id            = leadId;
        this.TyLeThanhCong      = tyLeThanhCong;
        this.DoanhThuKyVong     = doanhThuKyVong;
        this.GhiChu             = ghiChu;
        this.NgayDuKien         = ngayDuKien;
        this.NhanVienPhuTrach_Id = nhanVienPhuTrachId;
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
        return IsDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        IsDeleted = isDeleted;
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


    public CoHoiBanHang() {
    }


    /** Soft-delete */
    public void xoa() {
        this.IsDeleted = 1;
    }
}

package com.crm.domain.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SanPham {

    private Integer id;

    private Integer loaiSanPham;

    private String maSanPham;

    private String tenSanPham;

    private String donVi;

    private Double giaBan;


    private Integer slTon;

    private Integer trangThai;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public void ThemHinhAnh(SanPhamHinhAnh hinhAnh)
    {
        if (this.hinhAnh == null) {
            this.hinhAnh = new ArrayList<>();
        }
        this.hinhAnh.add(hinhAnh);
    }
    public List<SanPhamHinhAnh> getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(List<SanPhamHinhAnh> hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    private List<SanPhamHinhAnh> hinhAnh=new ArrayList<>();

    public Integer getSanPhamId() {
        return id;
    }

    public void setSanPhamId(Integer id) {
        this.id = id;
    }

    public Integer getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(Integer loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public Double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(Double giaBan) {
        this.giaBan = giaBan;
    }

    public Integer getSlTon() {
        return slTon;
    }

    public void setSlTon(Integer slTon) {
        this.slTon = slTon;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

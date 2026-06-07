package com.crm.domain.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bh_sanpham")
public class SanPham {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "VARCHAR(36)") // ✅ thêm vào
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Integer  Id;
    @Column(name = "loai_san_pham_id")
    private Integer loaiSanPham;
    @Column(name = "masp")
    private String maSanPham;

    @Column(name = "tensp")
    private String tenSanPham;

    @Column(name = "don_vi")
    private String donVi;

    @Column(name = "gia_ban")
    private Double giaBan;

    @Column(name = "so_luong_ton")
    private Integer slTon;
    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Integer  getSanPhamId() {
        return Id;
    }

    public void setSanPhamId(Integer  Id) {
        this.Id = Id;
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

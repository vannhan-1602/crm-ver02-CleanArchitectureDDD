package com.crm.persistence.jpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "HT_ThongTinNhanSu")
public class ThongTinNhanSuJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "HoTen", length = 100)
    private String hoTen;

    @Column(name = "Email", length = 150)
    private String email;

    @Column(name = "SoDienThoai", length = 20)
    private String soDienThoai;

    @Column(name = "PhongBan_Id")
    private Integer phongBanId;

    @Column(name = "ChucVu_Id")
    private Integer chucVuId;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    public ThongTinNhanSuJpaEntity() {}

    public Integer getId()   { return id; }
    public String getHoTen() { return hoTen; }
    public String getEmail() { return email; }
    public String getSoDienThoai() { return soDienThoai; }
    public Integer getPhongBanId() { return phongBanId; }
    public Integer getChucVuId() { return chucVuId; }
    public Integer getTrangThai() { return trangThai; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Integer id) { this.id = id; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public void setEmail(String email) { this.email = email; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public void setPhongBanId(Integer phongBanId) { this.phongBanId = phongBanId; }
    public void setChucVuId(Integer chucVuId) { this.chucVuId = chucVuId; }
    public void setTrangThai(Integer trangThai) { this.trangThai = trangThai; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

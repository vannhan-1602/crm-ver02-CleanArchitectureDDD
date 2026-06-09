package com.crm.persistence.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "KH_KhachHang")
public class KhachHangJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "MaKhachHang", nullable = false, length = 20, unique = true)
    private String maKhachHang;

    @Column(name = "TenKhachHang", nullable = false, length = 100)
    private String tenKhachHang;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "SoDienThoai", length = 20)
    private String soDienThoai;

    @Column(name = "NhanVienPhuTrach_Id")
    private Integer nhanVienPhuTrachId;

    @Column(name = "IsDeleted")
    private boolean isDeleted;

    @Column(name = "CreatedAt", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    @UpdateTimestamp
    private LocalDateTime updatedAt;



    public KhachHangJpaEntity() {}



    public Long getId()                             { return id; }
    public void setId(Long id)                      { this.id = id; }

    public String getMaKhachHang()                  { return maKhachHang; }
    public void setMaKhachHang(String v)            { this.maKhachHang = v; }

    public String getTenKhachHang()                 { return tenKhachHang; }
    public void setTenKhachHang(String v)           { this.tenKhachHang = v; }

    public String getEmail()                        { return email; }
    public void setEmail(String email)              { this.email = email; }

    public String getSoDienThoai()                  { return soDienThoai; }
    public void setSoDienThoai(String v)            { this.soDienThoai = v; }

    public Integer getNhanVienPhuTrachId()          { return nhanVienPhuTrachId; }
    public void setNhanVienPhuTrachId(Integer v)    { this.nhanVienPhuTrachId = v; }

    public boolean isDeleted()                      { return isDeleted; }
    public void setDeleted(boolean deleted)         { isDeleted = deleted; }

    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void setCreatedAt(LocalDateTime v)       { this.createdAt = v; }

    public LocalDateTime getUpdatedAt()             { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v)       { this.updatedAt = v; }
}
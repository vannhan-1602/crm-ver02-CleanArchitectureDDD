package com.crm.persistence.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "KH_HoatDong")
public class HoatDongJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "KhachHang_Id")
    private Long khachHangId;

    @Column(name = "Lead_Id")
    private Long leadId;

    @Column(name = "LoaiHoatDong", length = 20)
    private String loaiHoatDong;

    @Column(name = "NoiDung", length = 255)
    private String noiDung;

    @Column(name = "ThoiGianThucHien")
    private LocalDateTime thoiGianThucHien;

    @Column(name = "NhanVien_Id")
    private Integer nhanVienId;

    @Column(name = "CreatedAt", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public HoatDongJpaEntity() {}

    public Long getId()                           { return id; }
    public void setId(Long id)                    { this.id = id; }

    public Long getKhachHangId()                  { return khachHangId; }
    public void setKhachHangId(Long v)            { this.khachHangId = v; }

    public Long getLeadId()                       { return leadId; }
    public void setLeadId(Long v)                 { this.leadId = v; }

    public String getLoaiHoatDong()               { return loaiHoatDong; }
    public void setLoaiHoatDong(String v)         { this.loaiHoatDong = v; }

    public String getNoiDung()                    { return noiDung; }
    public void setNoiDung(String v)              { this.noiDung = v; }

    public LocalDateTime getThoiGianThucHien()    { return thoiGianThucHien; }
    public void setThoiGianThucHien(LocalDateTime v) { this.thoiGianThucHien = v; }

    public Integer getNhanVienId()                { return nhanVienId; }
    public void setNhanVienId(Integer v)          { this.nhanVienId = v; }

    public LocalDateTime getCreatedAt()           { return createdAt; }
    public void setCreatedAt(LocalDateTime v)     { this.createdAt = v; }

    public LocalDateTime getUpdatedAt()           { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v)     { this.updatedAt = v; }
}
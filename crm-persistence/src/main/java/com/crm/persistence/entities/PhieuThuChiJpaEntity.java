package com.crm.persistence.entities;

import com.crm.domain.valueobjects.LoaiPhieuThuChi;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "KT_PhieuThuChi")
public class PhieuThuChiJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "MaPhieu", nullable = false, length = 50)
    private String maPhieu;

    @Enumerated(EnumType.STRING)
    @Column(name = "LoaiPhieu", nullable = false, length = 10)
    private LoaiPhieuThuChi loaiPhieu;

    @Column(name = "KhachHang_Id")
    private Long khachHangId;

    @Column(name = "HoaDon_Id")
    private Long hoaDonId;

    @Column(name = "SoTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal soTien;

    @Column(name = "NguoiLap_Id")
    private Integer nguoiLapId;

    @Column(name = "NgayTao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    protected PhieuThuChiJpaEntity() {
    }

    public PhieuThuChiJpaEntity(Long id,
                                String maPhieu,
                                LoaiPhieuThuChi loaiPhieu,
                                Long khachHangId,
                                Long hoaDonId,
                                BigDecimal soTien,
                                Integer nguoiLapId,
                                LocalDateTime ngayTao,
                                LocalDateTime updatedAt) {
        this.id = id;
        this.maPhieu = maPhieu;
        this.loaiPhieu = loaiPhieu;
        this.khachHangId = khachHangId;
        this.hoaDonId = hoaDonId;
        this.soTien = soTien;
        this.nguoiLapId = nguoiLapId;
        this.ngayTao = ngayTao;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (ngayTao == null) {
            ngayTao = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public LoaiPhieuThuChi getLoaiPhieu() {
        return loaiPhieu;
    }

    public Long getKhachHangId() {
        return khachHangId;
    }

    public Long getHoaDonId() {
        return hoaDonId;
    }

    public BigDecimal getSoTien() {
        return soTien;
    }

    public Integer getNguoiLapId() {
        return nguoiLapId;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

package com.crm.persistence.entities;

import com.crm.domain.valueobjects.TrangThaiThanhToan;
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
@Table(name = "KT_HoaDon")
public class HoaDonJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "MaHoaDon", nullable = false, length = 50)
    private String maHoaDon;

    @Column(name = "HopDong_Id")
    private Long hopDongId;

    @Column(name = "KhachHang_Id", nullable = false)
    private Long khachHangId;

    @Column(name = "TongTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "SoTienDaThu", precision = 18, scale = 2)
    private BigDecimal soTienDaThu;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThaiThanhToan", nullable = false, length = 30)
    private TrangThaiThanhToan trangThaiThanhToan;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    protected HoaDonJpaEntity() {
    }

    public HoaDonJpaEntity(Long id,
                           String maHoaDon,
                           Long hopDongId,
                           Long khachHangId,
                           BigDecimal tongTien,
                           BigDecimal soTienDaThu,
                           TrangThaiThanhToan trangThaiThanhToan,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
        this.id = id;
        this.maHoaDon = maHoaDon;
        this.hopDongId = hopDongId;
        this.khachHangId = khachHangId;
        this.tongTien = tongTien;
        this.soTienDaThu = soTienDaThu;
        this.trangThaiThanhToan = trangThaiThanhToan;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
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

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public Long getHopDongId() {
        return hopDongId;
    }

    public Long getKhachHangId() {
        return khachHangId;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public BigDecimal getSoTienDaThu() {
        return soTienDaThu;
    }

    public TrangThaiThanhToan getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

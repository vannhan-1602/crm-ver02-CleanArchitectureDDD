package com.crm.persistence.entities;

import com.crm.domain.valueobjects.TrangThaiHopDong;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "HD_HopDong")
public class HopDongJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MaHopDong", nullable = false, length = 50)
    private String maHopDong;

    @Column(name = "KhachHang_Id", nullable = false)
    private Long khachHangId;

    @Column(name = "NgayKy")
    private LocalDate ngayKy;

    @Column(name = "ThoiHan")
    private Integer thoiHan;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai")
    private TrangThaiHopDong trangThai;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    protected HopDongJpaEntity() {
    }

    public HopDongJpaEntity(Long id,
                            String maHopDong,
                            Long khachHangId,
                            LocalDate ngayKy,
                            Integer thoiHan,
                            TrangThaiHopDong trangThai,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt) {
        this.id = id;
        this.maHopDong = maHopDong;
        this.khachHangId = khachHangId;
        this.ngayKy = ngayKy;
        this.thoiHan = thoiHan;
        this.trangThai = trangThai;
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

    public String getMaHopDong() {
        return maHopDong;
    }

    public Long getKhachHangId() {
        return khachHangId;
    }

    public LocalDate getNgayKy() {
        return ngayKy;
    }

    public Integer getThoiHan() {
        return thoiHan;
    }

    public TrangThaiHopDong getTrangThai() {
        return trangThai;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

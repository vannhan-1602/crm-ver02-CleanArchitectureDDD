package com.crm.domain.entities;

import com.crm.domain.valueobjects.MaHopDong;
import com.crm.domain.valueobjects.TrangThaiHopDong;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HopDong {
    private Long id;

    private MaHopDong maHopDong;

    private Long khachHangId;

    private LocalDate ngayKy;

    private Integer thoiHan;

    private TrangThaiHopDong trangThai;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public HopDong(MaHopDong maHopDong, Long khachHangId, LocalDate ngayKy, Integer thoiHan, TrangThaiHopDong trangThai) {
        this(null, maHopDong, khachHangId, ngayKy, thoiHan, trangThai, null, null);
    }

    public HopDong(Long id, MaHopDong maHopDong, Long khachHangId, LocalDate ngayKy, Integer thoiHan, TrangThaiHopDong trangThai, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.maHopDong = maHopDong;
        this.khachHangId = khachHangId;
        this.ngayKy = ngayKy;
        this.thoiHan = thoiHan;
        this.trangThai = trangThai != null ? trangThai : TrangThaiHopDong.DangThucHien;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public MaHopDong getMaHopDong() {
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

    public void changeMaHopDong(MaHopDong maHopDong) {
        if (maHopDong != null) {
            this.maHopDong = maHopDong;
        }
    }

    public void updateDetails(Long khachHangId, LocalDate ngayKy, Integer thoiHan, TrangThaiHopDong trangThai) {
        if (khachHangId != null) {
            this.khachHangId = khachHangId;
        }
        if (ngayKy != null) {
            this.ngayKy = ngayKy;
        }
        if (thoiHan != null && thoiHan >= 0) {
            this.thoiHan = thoiHan;
        }
        if (trangThai != null) {
            this.trangThai = trangThai;
        }
    }

    public void complete() {
        this.trangThai = TrangThaiHopDong.ThanhLy;
    }
}

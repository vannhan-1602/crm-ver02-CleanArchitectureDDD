package com.crm.domain.entities;

import com.crm.domain.valueobjects.MaHoaDon;
import com.crm.domain.valueobjects.TrangThaiThanhToan;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HoaDon {
    private Long id;
    private MaHoaDon maHoaDon;
    private Long hopDongId;
    private Long khachHangId;
    private BigDecimal tongTien;
    private BigDecimal soTienDaThu;
    private TrangThaiThanhToan trangThaiThanhToan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HoaDon(MaHoaDon maHoaDon,
                  Long hopDongId,
                  Long khachHangId,
                  BigDecimal tongTien,
                  BigDecimal soTienDaThu,
                  TrangThaiThanhToan trangThaiThanhToan) {
        this(null, maHoaDon, hopDongId, khachHangId, tongTien, soTienDaThu, trangThaiThanhToan, null, null);
    }

    public HoaDon(Long id,
                  MaHoaDon maHoaDon,
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
        this.khachHangId = validateRequiredId(khachHangId, "Khach hang");
        this.tongTien = validatePositive(tongTien, "Tong tien hoa don");
        this.soTienDaThu = soTienDaThu != null ? soTienDaThu : BigDecimal.ZERO;
        if (trangThaiThanhToan == null) {
            capNhatTrangThaiThanhToan(this.soTienDaThu);
        } else {
            this.trangThaiThanhToan = trangThaiThanhToan;
        }
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public MaHoaDon getMaHoaDon() {
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

    public void changeMaHoaDon(MaHoaDon maHoaDon) {
        if (maHoaDon != null) {
            this.maHoaDon = maHoaDon;
        }
    }

    public void updateDetails(Long hopDongId,
                              Long khachHangId,
                              BigDecimal tongTien,
                              TrangThaiThanhToan trangThaiThanhToan) {
        if (hopDongId != null) {
            this.hopDongId = hopDongId;
        }
        if (khachHangId != null) {
            this.khachHangId = validateRequiredId(khachHangId, "Khach hang");
        }
        if (tongTien != null) {
            this.tongTien = validatePositive(tongTien, "Tong tien hoa don");
        }
        if (trangThaiThanhToan != null) {
            this.trangThaiThanhToan = trangThaiThanhToan;
        }
    }

    public void capNhatTrangThaiThanhToan(BigDecimal tongDaThu) {
        BigDecimal daThu = tongDaThu != null ? tongDaThu : BigDecimal.ZERO;
        this.soTienDaThu = daThu;
        if (daThu.compareTo(BigDecimal.ZERO) <= 0) {
            this.trangThaiThanhToan = TrangThaiThanhToan.ChuaThanhToan;
            return;
        }
        if (daThu.compareTo(tongTien) >= 0) {
            this.trangThaiThanhToan = TrangThaiThanhToan.HoanTat;
            return;
        }
        this.trangThaiThanhToan = TrangThaiThanhToan.ThanhToan1Phan;
    }

    private Long validateRequiredId(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + " id khong hop le");
        }
        return value;
    }

    private BigDecimal validatePositive(BigDecimal value, String fieldName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(fieldName + " phai lon hon 0");
        }
        return value;
    }
}

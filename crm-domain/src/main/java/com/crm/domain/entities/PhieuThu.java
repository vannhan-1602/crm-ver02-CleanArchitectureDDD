package com.crm.domain.entities;

import com.crm.domain.valueobjects.MaPhieuThu;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PhieuThu {
    private Long id;
    private MaPhieuThu maPhieuThu;
    private Long khachHangId;
    private Long hoaDonId;
    private BigDecimal soTien;
    private Integer nguoiLapId;
    private LocalDateTime ngayTao;
    private LocalDateTime updatedAt;

    public PhieuThu(MaPhieuThu maPhieuThu,
                    Long khachHangId,
                    Long hoaDonId,
                    BigDecimal soTien,
                    Integer nguoiLapId) {
        this(null, maPhieuThu, khachHangId, hoaDonId, soTien, nguoiLapId, null, null);
    }

    public PhieuThu(Long id,
                    MaPhieuThu maPhieuThu,
                    Long khachHangId,
                    Long hoaDonId,
                    BigDecimal soTien,
                    Integer nguoiLapId,
                    LocalDateTime ngayTao,
                    LocalDateTime updatedAt) {
        this.id = id;
        this.maPhieuThu = maPhieuThu;
        this.khachHangId = khachHangId;
        this.hoaDonId = validateRequiredId(hoaDonId);
        this.soTien = validatePositive(soTien);
        this.nguoiLapId = nguoiLapId;
        this.ngayTao = ngayTao;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public MaPhieuThu getMaPhieuThu() {
        return maPhieuThu;
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

    public void changeMaPhieuThu(MaPhieuThu maPhieuThu) {
        if (maPhieuThu != null) {
            this.maPhieuThu = maPhieuThu;
        }
    }

    public void updateDetails(Long khachHangId,
                              Long hoaDonId,
                              BigDecimal soTien,
                              Integer nguoiLapId) {
        if (khachHangId != null) {
            this.khachHangId = khachHangId;
        }
        if (hoaDonId != null) {
            this.hoaDonId = validateRequiredId(hoaDonId);
        }
        if (soTien != null) {
            this.soTien = validatePositive(soTien);
        }
        if (nguoiLapId != null) {
            this.nguoiLapId = nguoiLapId;
        }
    }

    private Long validateRequiredId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Hoa don id khong hop le");
        }
        return value;
    }

    private BigDecimal validatePositive(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("So tien phai lon hon 0");
        }
        return value;
    }
}

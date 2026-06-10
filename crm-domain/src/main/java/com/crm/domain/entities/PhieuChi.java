package com.crm.domain.entities;

import com.crm.domain.valueobjects.MaPhieuChi;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PhieuChi {
    private Long id;
    private MaPhieuChi maPhieuChi;
    private Long khachHangId;
    private Long hoaDonId;
    private BigDecimal soTien;
    private Integer nguoiLapId;
    private LocalDateTime ngayTao;
    private LocalDateTime updatedAt;

    public PhieuChi(MaPhieuChi maPhieuChi,
                    Long khachHangId,
                    Long hoaDonId,
                    BigDecimal soTien,
                    Integer nguoiLapId) {
        this(null, maPhieuChi, khachHangId, hoaDonId, soTien, nguoiLapId, null, null);
    }

    public PhieuChi(Long id,
                    MaPhieuChi maPhieuChi,
                    Long khachHangId,
                    Long hoaDonId,
                    BigDecimal soTien,
                    Integer nguoiLapId,
                    LocalDateTime ngayTao,
                    LocalDateTime updatedAt) {
        this.id = id;
        this.maPhieuChi = maPhieuChi;
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

    public MaPhieuChi getMaPhieuChi() {
        return maPhieuChi;
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

    public void changeMaPhieuChi(MaPhieuChi maPhieuChi) {
        if (maPhieuChi != null) {
            this.maPhieuChi = maPhieuChi;
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

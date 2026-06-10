package com.crm.application.phieuchi.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.PhieuChi;

import java.math.BigDecimal;

public class UpdatePhieuChiCommand implements IRequest<PhieuChi> {
    private Long id;
    private String maPhieuChi;
    private Long khachHangId;
    private Long hoaDonId;
    private BigDecimal soTien;
    private Integer nguoiLapId;

    public UpdatePhieuChiCommand(String maPhieuChi,
                                 Long khachHangId,
                                 Long hoaDonId,
                                 BigDecimal soTien,
                                 Integer nguoiLapId) {
        this.maPhieuChi = maPhieuChi;
        this.khachHangId = khachHangId;
        this.hoaDonId = hoaDonId;
        this.soTien = soTien;
        this.nguoiLapId = nguoiLapId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaPhieuChi() {
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
}

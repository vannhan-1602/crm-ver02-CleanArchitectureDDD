package com.crm.application.phieuthu.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.PhieuThu;

import java.math.BigDecimal;

public class UpdatePhieuThuCommand implements IRequest<PhieuThu> {
    private Long id;
    private String maPhieuThu;
    private Long khachHangId;
    private Long hoaDonId;
    private BigDecimal soTien;
    private Integer nguoiLapId;

    public UpdatePhieuThuCommand(String maPhieuThu,
                                 Long khachHangId,
                                 Long hoaDonId,
                                 BigDecimal soTien,
                                 Integer nguoiLapId) {
        this.maPhieuThu = maPhieuThu;
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

    public String getMaPhieuThu() {
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
}

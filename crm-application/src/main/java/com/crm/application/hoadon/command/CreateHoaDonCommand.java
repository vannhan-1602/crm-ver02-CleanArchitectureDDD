package com.crm.application.hoadon.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.HoaDon;

import java.math.BigDecimal;

public class CreateHoaDonCommand implements IRequest<HoaDon> {
    private String maHoaDon;
    private Long hopDongId;
    private Long khachHangId;
    private BigDecimal tongTien;
    private BigDecimal soTienDaThu;
    private String trangThaiThanhToan;

    public CreateHoaDonCommand(String maHoaDon,
                               Long hopDongId,
                               Long khachHangId,
                               BigDecimal tongTien,
                               BigDecimal soTienDaThu,
                               String trangThaiThanhToan) {
        this.maHoaDon = maHoaDon;
        this.hopDongId = hopDongId;
        this.khachHangId = khachHangId;
        this.tongTien = tongTien;
        this.soTienDaThu = soTienDaThu;
        this.trangThaiThanhToan = trangThaiThanhToan;
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

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }
}

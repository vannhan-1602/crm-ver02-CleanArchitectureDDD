package com.crm.application.hoadon.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.HoaDon;

import java.math.BigDecimal;

public class UpdateHoaDonCommand implements IRequest<HoaDon> {
    private Long id;
    private String maHoaDon;
    private Long hopDongId;
    private Long khachHangId;
    private BigDecimal tongTien;
    private String trangThaiThanhToan;

    public UpdateHoaDonCommand(String maHoaDon,
                               Long hopDongId,
                               Long khachHangId,
                               BigDecimal tongTien,
                               String trangThaiThanhToan) {
        this.maHoaDon = maHoaDon;
        this.hopDongId = hopDongId;
        this.khachHangId = khachHangId;
        this.tongTien = tongTien;
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }
}

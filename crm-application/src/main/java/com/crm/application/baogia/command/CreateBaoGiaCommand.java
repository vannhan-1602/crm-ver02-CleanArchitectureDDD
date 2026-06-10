package com.crm.application.baogia.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.BaoGia;

import java.util.List;

public class CreateBaoGiaCommand implements IRequest<BaoGia> {
    private String maBaoGia;
    private Long khachHangId;
    private Integer nhanVienId;
    private String trangThai;
    private List<BaoGiaChiTietCommand> chiTiets;

    public CreateBaoGiaCommand() {
    }

    public CreateBaoGiaCommand(String maBaoGia,
                               Long khachHangId,
                               Integer nhanVienId,
                               String trangThai,
                               List<BaoGiaChiTietCommand> chiTiets) {
        this.maBaoGia = maBaoGia;
        this.khachHangId = khachHangId;
        this.nhanVienId = nhanVienId;
        this.trangThai = trangThai;
        this.chiTiets = chiTiets;
    }

    public String getMaBaoGia() {
        return maBaoGia;
    }

    public Long getKhachHangId() {
        return khachHangId;
    }

    public Integer getNhanVienId() {
        return nhanVienId;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public List<BaoGiaChiTietCommand> getChiTiets() {
        return chiTiets;
    }
}

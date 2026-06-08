package com.crm.application.hopdong.command;

import java.time.LocalDate;

public class UpdateHopDongCommand {
    private String maHopDong;
    private Long khachHangId;
    private LocalDate ngayKy;
    private Integer thoiHan;
    private String trangThai;

    public UpdateHopDongCommand() {
    }

    public UpdateHopDongCommand(String maHopDong, Long khachHangId, LocalDate ngayKy, Integer thoiHan, String trangThai) {
        this.maHopDong = maHopDong;
        this.khachHangId = khachHangId;
        this.ngayKy = ngayKy;
        this.thoiHan = thoiHan;
        this.trangThai = trangThai;
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

    public String getTrangThai() {
        return trangThai;
    }
}

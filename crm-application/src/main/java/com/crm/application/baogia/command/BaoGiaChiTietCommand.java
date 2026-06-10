package com.crm.application.baogia.command;

public class BaoGiaChiTietCommand {
    private Integer sanPhamId;
    private Integer soLuong;
    private Double donGia;

    public BaoGiaChiTietCommand() {
    }

    public BaoGiaChiTietCommand(Integer sanPhamId, Integer soLuong, Double donGia) {
        this.sanPhamId = sanPhamId;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public Integer getSanPhamId() {
        return sanPhamId;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public Double getDonGia() {
        return donGia;
    }
}

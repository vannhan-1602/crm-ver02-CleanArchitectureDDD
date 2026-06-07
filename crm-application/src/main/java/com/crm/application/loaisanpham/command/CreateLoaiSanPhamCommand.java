package com.crm.application.loaisanpham.command;

public class CreateLoaiSanPhamCommand {
    private String tenLoai;
    private String moTa;

    public CreateLoaiSanPhamCommand() {}
    public CreateLoaiSanPhamCommand(String tenLoai, String moTa) {
        this.tenLoai = tenLoai;
        this.moTa = moTa;
    }

    public String getTenLoai() { return tenLoai; }
    public String getMoTa() { return moTa; }
}
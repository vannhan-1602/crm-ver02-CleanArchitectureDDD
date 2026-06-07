package com.crm.application.loaisanpham.command;

import java.util.UUID;

public class UpdateLoaiSanPhamCommand {
    private Integer  id;
    private String tenLoai;
    private String moTa;

    public UpdateLoaiSanPhamCommand() {}
    public UpdateLoaiSanPhamCommand(Integer  id, String tenLoai, String moTa) {
        this.id = id;
        this.tenLoai = tenLoai;
        this.moTa = moTa;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTenLoai() { return tenLoai; }
    public String getMoTa() { return moTa; }
}
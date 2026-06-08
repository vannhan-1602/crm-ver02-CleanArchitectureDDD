package com.crm.application.loaisanpham.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.LoaiSanPham;

public class CreateLoaiSanPhamCommand implements IRequest<LoaiSanPham> {
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

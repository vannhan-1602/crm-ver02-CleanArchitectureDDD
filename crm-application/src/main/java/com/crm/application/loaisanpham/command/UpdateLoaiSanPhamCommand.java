package com.crm.application.loaisanpham.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.LoaiSanPham;

import java.util.List;
import java.util.UUID;

public class UpdateLoaiSanPhamCommand implements IRequest<LoaiSanPham> {
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
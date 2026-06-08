package com.crm.application.sanpham.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.SanPham;

import java.util.UUID;

public class UpdateSanPhamCommand implements IRequest<SanPham> {
    private Integer  id;
    private Integer loaiSanPham;
    private String maSanPham;
    private String tenSanPham;
    private String donVi;
    private Double giaBan;
    private Integer slTon;
    private Integer trangThai;

    public UpdateSanPhamCommand() {}

    public UpdateSanPhamCommand(Integer  id, Integer loaiSanPham, String maSanPham, String tenSanPham,
                                String donVi, Double giaBan, Integer slTon, Integer trangThai) {
        this.id = id;
        this.loaiSanPham = loaiSanPham;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.donVi = donVi;
        this.giaBan = giaBan;
        this.slTon = slTon;
        this.trangThai = trangThai;
    }

    public Integer getId() { return id; }
    public void setId(Integer  id) { this.id = id; }
    public Integer getLoaiSanPham() { return loaiSanPham; }
    public String getMaSanPham() { return maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public String getDonVi() { return donVi; }
    public Double getGiaBan() { return giaBan; }
    public Integer getSlTon() { return slTon; }
    public Integer getTrangThai() { return trangThai; }
}
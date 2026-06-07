package com.crm.application.sanpham.command;

public class CreateSanPhamCommand {
    private Integer loaiSanPham;
    private String maSanPham;
    private String tenSanPham;
    private String donVi;
    private Double giaBan;
    private Integer slTon;
    private Integer trangThai;

    public CreateSanPhamCommand() {}

    public CreateSanPhamCommand(Integer loaiSanPham, String maSanPham, String tenSanPham,
                                String donVi, Double giaBan, Integer slTon, Integer trangThai) {
        this.loaiSanPham = loaiSanPham;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.donVi = donVi;
        this.giaBan = giaBan;
        this.slTon = slTon;
        this.trangThai = trangThai;
    }

    public Integer getLoaiSanPham() { return loaiSanPham; }
    public String getMaSanPham() { return maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public String getDonVi() { return donVi; }
    public Double getGiaBan() { return giaBan; }
    public Integer getSlTon() { return slTon; }
    public Integer getTrangThai() { return trangThai; }
}
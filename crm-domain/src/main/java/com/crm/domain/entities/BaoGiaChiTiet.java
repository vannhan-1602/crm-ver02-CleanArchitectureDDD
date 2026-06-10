package com.crm.domain.entities;

public class BaoGiaChiTiet {
    private Long id;
    private Integer sanPhamId;
    private Integer soLuong;
    private Double donGia;

    public BaoGiaChiTiet(Integer sanPhamId, Integer soLuong, Double donGia) {
        this(null, sanPhamId, soLuong, donGia);
    }

    public BaoGiaChiTiet(Long id, Integer sanPhamId, Integer soLuong, Double donGia) {
        this.id = id;
        this.sanPhamId = sanPhamId;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setSanPhamId(Integer sanPhamId) {
        this.sanPhamId = sanPhamId;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }
}

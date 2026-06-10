package com.crm.domain.entities;

import com.crm.domain.valueobjects.TrangThaiBaoGia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BaoGia {
    private Long id;
    private String maBaoGia;
    private Long khachHangId;
    private Integer nhanVienId;
    private Double tongTien;
    private TrangThaiBaoGia trangThai;
    private List<BaoGiaChiTiet> chiTiets = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BaoGia(String maBaoGia,
                  Long khachHangId,
                  Integer nhanVienId,
                  TrangThaiBaoGia trangThai,
                  List<BaoGiaChiTiet> chiTiets) {
        this(null, maBaoGia, khachHangId, nhanVienId, 0D, trangThai, chiTiets, null, null);
    }

    public BaoGia(Long id,
                  String maBaoGia,
                  Long khachHangId,
                  Integer nhanVienId,
                  Double tongTien,
                  TrangThaiBaoGia trangThai,
                  List<BaoGiaChiTiet> chiTiets,
                  LocalDateTime createdAt,
                  LocalDateTime updatedAt) {
        this.id = id;
        this.maBaoGia = maBaoGia;
        this.khachHangId = khachHangId;
        this.nhanVienId = nhanVienId;
        this.tongTien = tongTien != null ? tongTien : 0D;
        this.trangThai = trangThai != null ? trangThai : TrangThaiBaoGia.Nhap;
        setChiTiets(chiTiets);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        tinhTongTien();
    }

    public void capNhatThongTin(String maBaoGia,
                                Long khachHangId,
                                Integer nhanVienId,
                                TrangThaiBaoGia trangThai) {
        if (maBaoGia != null && !maBaoGia.isBlank()) {
            this.maBaoGia = maBaoGia.trim();
        }
        if (khachHangId != null) {
            this.khachHangId = khachHangId;
        }
        if (nhanVienId != null) {
            this.nhanVienId = nhanVienId;
        }
        if (trangThai != null) {
            this.trangThai = trangThai;
        }
    }

    public void setChiTiets(List<BaoGiaChiTiet> chiTiets) {
        this.chiTiets = new ArrayList<>();
        if (chiTiets != null) {
            this.chiTiets.addAll(chiTiets);
        }
        tinhTongTien();
    }

    public Double tinhTongTien() {
        double total = 0D;
        for (BaoGiaChiTiet item : chiTiets) {
            if (item == null) {
                continue;
            }
            int soLuong = item.getSoLuong() != null ? item.getSoLuong() : 0;
            double donGia = item.getDonGia() != null ? item.getDonGia() : 0D;
            total += soLuong * donGia;
        }
        this.tongTien = total;
        return total;
    }

    public Long getId() {
        return id;
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

    public Double getTongTien() {
        return tongTien;
    }

    public TrangThaiBaoGia getTrangThai() {
        return trangThai;
    }

    public List<BaoGiaChiTiet> getChiTiets() {
        return chiTiets;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

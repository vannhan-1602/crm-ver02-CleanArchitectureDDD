package com.crm.persistence.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "KH_DiaChi")
public class DiaChiJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "KhachHang_Id", nullable = false)
    private Long khachHangId;

    @Column(name = "DiaChiChiTiet", length = 150)
    private String diaChiChiTiet;

    @Column(name = "TinhThanh", length = 50)
    private String tinhThanh;

    @Column(name = "QuanHuyen", length = 50)
    private String quanHuyen;

    @Column(name = "PhuongXa", length = 50)
    private String phuongXa;

    @Column(name = "LoaiDiaChi")
    private String loaiDiaChi;

    @Column(name = "IsDefault")
    private boolean isDefault;

    public DiaChiJpaEntity() {}

    public Long getId()                            { return id; }
    public void setId(Long id)                     { this.id = id; }

    public Long getKhachHangId()                   { return khachHangId; }
    public void setKhachHangId(Long v)             { this.khachHangId = v; }

    public String getDiaChiChiTiet()               { return diaChiChiTiet; }
    public void setDiaChiChiTiet(String v)         { this.diaChiChiTiet = v; }

    public String getTinhThanh()                   { return tinhThanh; }
    public void setTinhThanh(String v)             { this.tinhThanh = v; }

    public String getQuanHuyen()                   { return quanHuyen; }
    public void setQuanHuyen(String v)             { this.quanHuyen = v; }

    public String getPhuongXa()                    { return phuongXa; }
    public void setPhuongXa(String v)              { this.phuongXa = v; }

    public String getLoaiDiaChi()                  { return loaiDiaChi; }
    public void setLoaiDiaChi(String v)            { this.loaiDiaChi = v; }

    public boolean isDefault()                     { return isDefault; }
    public void setDefault(boolean v)              { this.isDefault = v; }
}
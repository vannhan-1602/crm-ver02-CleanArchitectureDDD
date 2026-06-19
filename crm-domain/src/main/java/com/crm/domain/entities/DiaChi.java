package com.crm.domain.entities;

public class DiaChi {

    private Long id;
    private Long khachHangId;
    private String diaChiChiTiet;
    private String tinhThanh;
    private String quanHuyen;
    private String phuongXa;
    private String loaiDiaChi; 
    private boolean isDefault;

    public DiaChi(Long id, Long khachHangId, String diaChiChiTiet, String tinhThanh,
                  String quanHuyen, String phuongXa, String loaiDiaChi, boolean isDefault) {
        this.id = id;
        this.khachHangId = khachHangId;
        this.diaChiChiTiet = diaChiChiTiet;
        this.tinhThanh = tinhThanh;
        this.quanHuyen = quanHuyen;
        this.phuongXa = phuongXa;
        this.loaiDiaChi = loaiDiaChi;
        this.isDefault = isDefault;
    }

    public Long getId()              { return id; }
    public Long getKhachHangId()     { return khachHangId; }
    public String getDiaChiChiTiet() { return diaChiChiTiet; }
    public String getTinhThanh()     { return tinhThanh; }
    public String getQuanHuyen()     { return quanHuyen; }
    public String getPhuongXa()      { return phuongXa; }
    public String getLoaiDiaChi()    { return loaiDiaChi; }
    public boolean isDefault()       { return isDefault; }
}
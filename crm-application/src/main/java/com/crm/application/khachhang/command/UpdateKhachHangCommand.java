package com.crm.application.khachhang.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.KhachHang;

public class UpdateKhachHangCommand implements IRequest<KhachHang> {

    private Long id;
    private String tenKhachHang;
    private String email;
    private String soDienThoai;
    private Integer loaiKhachHangId;
    private Integer tinhTrangId;
    private String maSoThue;
    private Integer nhanVienPhuTrachId;

    public UpdateKhachHangCommand() {}

    public UpdateKhachHangCommand(String tenKhachHang,
                                  String email,
                                  String soDienThoai,
                                  Integer loaiKhachHangId,
                                  Integer tinhTrangId,
                                  String maSoThue,
                                  Integer nhanVienPhuTrachId) {
        this.tenKhachHang        = tenKhachHang;
        this.email               = email;
        this.soDienThoai         = soDienThoai;
        this.loaiKhachHangId     = loaiKhachHangId;
        this.tinhTrangId         = tinhTrangId;
        this.maSoThue            = maSoThue;
        this.nhanVienPhuTrachId  = nhanVienPhuTrachId;
    }

    public Long getId()                     { return id; }
    public void setId(Long id)              { this.id = id; }
    public String getTenKhachHang()         { return tenKhachHang; }
    public String getEmail()                { return email; }
    public String getSoDienThoai()          { return soDienThoai; }
    public Integer getLoaiKhachHangId()     { return loaiKhachHangId; }
    public Integer getTinhTrangId()         { return tinhTrangId; }
    public String getMaSoThue()             { return maSoThue; }
    public Integer getNhanVienPhuTrachId()  { return nhanVienPhuTrachId; }
}
package com.crm.application.khachhang.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.KhachHang;

public class CreateKhachHangCommand implements IRequest<KhachHang> {

    private String tenKhachHang;
    private String email;
    private String soDienThoai;
    private Integer loaiKhachHangId;
    private Integer tinhTrangId;
    private String maSoThue;
    private Integer nhanVienPhuTrachId;

    public CreateKhachHangCommand() {}

    public CreateKhachHangCommand(String tenKhachHang,
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

    public String getTenKhachHang()         { return tenKhachHang; }
    public String getEmail()                { return email; }
    public String getSoDienThoai()          { return soDienThoai; }
    public Integer getLoaiKhachHangId()     { return loaiKhachHangId; }
    public Integer getTinhTrangId()         { return tinhTrangId; }
    public String getMaSoThue()             { return maSoThue; }
    public Integer getNhanVienPhuTrachId()  { return nhanVienPhuTrachId; }
}
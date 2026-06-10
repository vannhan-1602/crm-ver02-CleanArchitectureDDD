package com.crm.domain.entities;

import java.time.LocalDateTime;

public class KhachHang {

    private Long id;


    private String maKhachHang;

    private String tenKhachHang;

    private String email;

    private String soDienThoai;
    private Integer loaiKhachHangId;
    private Integer tinhTrangId;
    private String maSoThue;

    private Integer nhanVienPhuTrachId;

    private boolean isDeleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public KhachHang(String maKhachHang,
                     String tenKhachHang,
                     String email,
                     String soDienThoai,
                     Integer loaiKhachHangId,
                     Integer tinhTrangId,
                     String maSoThue,
                     Integer nhanVienPhuTrachId) {

        this(
                null,
                maKhachHang,
                tenKhachHang,
                email,
                soDienThoai,
                loaiKhachHangId,
                tinhTrangId,
                maSoThue,
                nhanVienPhuTrachId,
                false,
                null,
                null
        );
    }


    public KhachHang(Long id,
                     String maKhachHang,
                     String tenKhachHang,
                     String email,
                     String soDienThoai,
                     Integer loaiKhachHangId,
                     Integer tinhTrangId,
                     String maSoThue,
                     Integer nhanVienPhuTrachId,
                     boolean isDeleted,
                     LocalDateTime createdAt,
                     LocalDateTime updatedAt) {

        if (tenKhachHang == null || tenKhachHang.isBlank()) {
            throw new IllegalArgumentException("TenKhachHang khong duoc de trong");
        }

        if (maKhachHang == null || maKhachHang.isBlank()) {
            throw new IllegalArgumentException("MaKhachHang khong duoc de trong");
        }

        this.id = id;
        this.maKhachHang = maKhachHang.trim();
        this.tenKhachHang = tenKhachHang.trim();
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.loaiKhachHangId = loaiKhachHangId;
        this.tinhTrangId = tinhTrangId;
        this.maSoThue = maSoThue;
        this.nhanVienPhuTrachId = nhanVienPhuTrachId;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

   

    public Long getId()                       { return id; }
    public String getMaKhachHang()            { return maKhachHang; }
    public String getTenKhachHang()           { return tenKhachHang; }
    public String getEmail()                  { return email; }
    public String getSoDienThoai()            { return soDienThoai; }
    public Integer getNhanVienPhuTrachId()    { return nhanVienPhuTrachId; }
    public boolean isDeleted()               { return isDeleted; }
    public LocalDateTime getCreatedAt()      { return createdAt; }
    public LocalDateTime getUpdatedAt()      { return updatedAt; }
    public Integer getLoaiKhachHangId()     {return loaiKhachHangId;}
    public Integer getTinhTrangId()         {return tinhTrangId;}
    public String getMaSoThue()             {return maSoThue;}

}
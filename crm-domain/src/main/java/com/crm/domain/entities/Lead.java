package com.crm.domain.entities;

import com.crm.domain.valueobjects.TinhTrangLead;

import java.time.LocalDateTime;


public class Lead {

    private Long id;


    private String tenLead;


    private String tenCongTy;

    private String soDienThoai;

    private String email;

    private TinhTrangLead tinhTrang;


    private Integer nhanVienPhuTrachId;


    private boolean isDeleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



    public Lead(String tenLead, String tenCongTy, String soDienThoai,
                String email, Integer nhanVienPhuTrachId) {
        this(null, tenLead, tenCongTy, soDienThoai, email,
                TinhTrangLead.Moi, nhanVienPhuTrachId, false, null, null);
    }


    public Lead(Long id, String tenLead, String tenCongTy, String soDienThoai,
                String email, TinhTrangLead tinhTrang, Integer nhanVienPhuTrachId,
                boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {

        if (tenLead == null || tenLead.isBlank()) {
            throw new IllegalArgumentException("TenLead khong duoc de trong");
        }

        this.id                  = id;
        this.tenLead             = tenLead.trim();
        this.tenCongTy           = tenCongTy;
        this.soDienThoai         = soDienThoai;
        this.email               = email;
        this.tinhTrang           = tinhTrang != null ? tinhTrang : TinhTrangLead.Moi;
        this.nhanVienPhuTrachId  = nhanVienPhuTrachId;
        this.isDeleted           = isDeleted;
        this.createdAt           = createdAt;
        this.updatedAt           = updatedAt;
    }




    public void updateThongTin(String tenLead, String tenCongTy,
                               String soDienThoai, String email,
                               Integer nhanVienPhuTrachId) {
        if (this.isDeleted) {
            throw new IllegalStateException("Khong the cap nhat Lead da bi xoa");
        }
        if (tenLead != null && !tenLead.isBlank()) {
            this.tenLead = tenLead.trim();
        }
        if (tenCongTy != null) {
            this.tenCongTy = tenCongTy;
        }
        if (soDienThoai != null) {
            this.soDienThoai = soDienThoai;
        }
        if (email != null) {
            this.email = email;
        }
        if (nhanVienPhuTrachId != null) {
            this.nhanVienPhuTrachId = nhanVienPhuTrachId;
        }
    }


    public void chuyenTrangThai(TinhTrangLead trangThaiMoi) {
        if (this.isDeleted) {
            throw new IllegalStateException("Khong the chuyen trang thai Lead da bi xoa");
        }
        if (!this.tinhTrang.canTransitionTo(trangThaiMoi)) {
            throw new IllegalStateException(
                    "Khong the chuyen tu " + this.tinhTrang + " sang " + trangThaiMoi
            );
        }
        this.tinhTrang = trangThaiMoi;
    }


    public void danhDauDaConvert() {
        if (this.isDeleted) {
            throw new IllegalStateException("Khong the convert Lead da bi xoa");
        }
        if (!this.tinhTrang.coTheConvert()) {
            throw new IllegalStateException(
                    "Chi co the convert Lead dang o trang thai DangChamSoc. " +
                            "Trang thai hien tai: " + this.tinhTrang
            );
        }
        this.tinhTrang = TinhTrangLead.DaChuyenDoi;
    }


    public void xoa() {
        if (this.tinhTrang == TinhTrangLead.DaChuyenDoi) {
            throw new IllegalStateException(
                    "Khong the xoa Lead da duoc chuyen doi thanh KhachHang"
            );
        }
        this.isDeleted = true;
    }

    
    public Long getId()                       { return id; }
    public String getTenLead()                { return tenLead; }
    public String getTenCongTy()              { return tenCongTy; }
    public String getSoDienThoai()            { return soDienThoai; }
    public String getEmail()                  { return email; }
    public TinhTrangLead getTinhTrang()        { return tinhTrang; }
    public Integer getNhanVienPhuTrachId()     { return nhanVienPhuTrachId; }
    public boolean isDeleted()                { return isDeleted; }
    public LocalDateTime getCreatedAt()       { return createdAt; }
    public LocalDateTime getUpdatedAt()       { return updatedAt; }
}
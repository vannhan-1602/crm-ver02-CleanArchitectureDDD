package com.crm.domain.entities;

import com.crm.domain.valueobjects.LoaiHoatDong;

import java.time.LocalDateTime;

public class HoatDong {

    private Long id;
    private Long khachHangId;
    private Long leadId;
    private LoaiHoatDong loaiHoatDong;
    private String noiDung;
    private LocalDateTime thoiGianThucHien;
    private Integer nhanVienId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

   
    public HoatDong(Long khachHangId,
                    Long leadId,
                    LoaiHoatDong loaiHoatDong,
                    String noiDung,
                    LocalDateTime thoiGianThucHien,
                    Integer nhanVienId) {
        this(null, khachHangId, leadId, loaiHoatDong, noiDung,
                thoiGianThucHien, nhanVienId, null, null);
    }


    public HoatDong(Long id,
                    Long khachHangId,
                    Long leadId,
                    LoaiHoatDong loaiHoatDong,
                    String noiDung,
                    LocalDateTime thoiGianThucHien,
                    Integer nhanVienId,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt) {

        if (khachHangId == null && leadId == null) {
            throw new IllegalArgumentException("HoatDong phai thuoc ve KhachHang hoac Lead");
        }
        if (loaiHoatDong == null) {
            throw new IllegalArgumentException("LoaiHoatDong khong duoc de trong");
        }
        if (noiDung == null || noiDung.isBlank()) {
            throw new IllegalArgumentException("NoiDung hoat dong khong duoc de trong");
        }
        if (thoiGianThucHien == null) {
            throw new IllegalArgumentException("ThoiGianThucHien khong duoc de trong");
        }

        this.id                 = id;
        this.khachHangId        = khachHangId;
        this.leadId             = leadId;
        this.loaiHoatDong       = loaiHoatDong;
        this.noiDung            = noiDung.trim();
        this.thoiGianThucHien   = thoiGianThucHien;
        this.nhanVienId         = nhanVienId;
        this.createdAt          = createdAt;
        this.updatedAt          = updatedAt;
    }



    public void capNhat(LoaiHoatDong loaiHoatDong,
                        String noiDung,
                        LocalDateTime thoiGianThucHien,
                        Integer nhanVienId) {
        if (loaiHoatDong != null) {
            this.loaiHoatDong = loaiHoatDong;
        }
        if (noiDung != null && !noiDung.isBlank()) {
            this.noiDung = noiDung.trim();
        }
        if (thoiGianThucHien != null) {
            this.thoiGianThucHien = thoiGianThucHien;
        }
        if (nhanVienId != null) {
            this.nhanVienId = nhanVienId;
        }
    }


    public Long getId()                           { return id; }
    public Long getKhachHangId()                  { return khachHangId; }
    public Long getLeadId()                       { return leadId; }
    public LoaiHoatDong getLoaiHoatDong()         { return loaiHoatDong; }
    public String getNoiDung()                    { return noiDung; }
    public LocalDateTime getThoiGianThucHien()    { return thoiGianThucHien; }
    public Integer getNhanVienId()                { return nhanVienId; }
    public LocalDateTime getCreatedAt()           { return createdAt; }
    public LocalDateTime getUpdatedAt()           { return updatedAt; }
}
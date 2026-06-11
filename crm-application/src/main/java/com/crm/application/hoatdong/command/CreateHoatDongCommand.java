package com.crm.application.hoatdong.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.HoatDong;

import java.time.LocalDateTime;

public class CreateHoatDongCommand implements IRequest<HoatDong> {

    private Long khachHangId;
    private Long leadId;
    private String loaiHoatDong;
    private String noiDung;
    private LocalDateTime thoiGianThucHien;
    private Integer nhanVienId;

    public CreateHoatDongCommand() {}

    public CreateHoatDongCommand(Long khachHangId,
                                 Long leadId,
                                 String loaiHoatDong,
                                 String noiDung,
                                 LocalDateTime thoiGianThucHien,
                                 Integer nhanVienId) {
        this.khachHangId       = khachHangId;
        this.leadId            = leadId;
        this.loaiHoatDong      = loaiHoatDong;
        this.noiDung           = noiDung;
        this.thoiGianThucHien  = thoiGianThucHien;
        this.nhanVienId        = nhanVienId;
    }

    public Long getKhachHangId()              { return khachHangId; }
    public Long getLeadId()                   { return leadId; }
    public String getLoaiHoatDong()           { return loaiHoatDong; }
    public String getNoiDung()                { return noiDung; }
    public LocalDateTime getThoiGianThucHien(){ return thoiGianThucHien; }
    public Integer getNhanVienId()            { return nhanVienId; }
}
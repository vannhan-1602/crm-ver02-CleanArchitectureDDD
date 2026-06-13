package com.crm.application.hoatdong.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.HoatDong;

import java.time.LocalDateTime;

public class UpdateHoatDongCommand implements IRequest<HoatDong> {

    private Long id;
    private String loaiHoatDong;
    private String noiDung;
    private LocalDateTime thoiGianThucHien;
    private Integer nhanVienId;

    public UpdateHoatDongCommand() {}

    public UpdateHoatDongCommand(String loaiHoatDong,
                                 String noiDung,
                                 LocalDateTime thoiGianThucHien,
                                 Integer nhanVienId) {
        this.loaiHoatDong     = loaiHoatDong;
        this.noiDung          = noiDung;
        this.thoiGianThucHien = thoiGianThucHien;
        this.nhanVienId       = nhanVienId;
    }

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }
    public String getLoaiHoatDong()            { return loaiHoatDong; }
    public String getNoiDung()                 { return noiDung; }
    public LocalDateTime getThoiGianThucHien() { return thoiGianThucHien; }
    public Integer getNhanVienId()             { return nhanVienId; }
}
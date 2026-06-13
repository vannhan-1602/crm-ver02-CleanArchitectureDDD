package com.crm.application.ticketphanhoi.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.TicketPhanHoi;

public class CreateTicketPhanHoiCommand implements IRequest<TicketPhanHoi> {
    private Long ticketId;
    private Integer nguoiPhanHoiId;
    private String loaiPhanHoi;
    private String noiDung;
    private String fileDinhKem;
    private String trangThaiSau;

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
    public Integer getNguoiPhanHoiId() { return nguoiPhanHoiId; }
    public void setNguoiPhanHoiId(Integer nguoiPhanHoiId) { this.nguoiPhanHoiId = nguoiPhanHoiId; }
    public String getLoaiPhanHoi() { return loaiPhanHoi; }
    public void setLoaiPhanHoi(String loaiPhanHoi) { this.loaiPhanHoi = loaiPhanHoi; }
    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
    public String getFileDinhKem() { return fileDinhKem; }
    public void setFileDinhKem(String fileDinhKem) { this.fileDinhKem = fileDinhKem; }
    public String getTrangThaiSau() { return trangThaiSau; }
    public void setTrangThaiSau(String trangThaiSau) { this.trangThaiSau = trangThaiSau; }
}

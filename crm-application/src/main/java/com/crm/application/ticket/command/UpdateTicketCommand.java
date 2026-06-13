package com.crm.application.ticket.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.Ticket;

import java.time.LocalDateTime;

public class UpdateTicketCommand implements IRequest<Ticket> {
    private Long id;
    private String tieuDe;
    private String moTa;
    private String fileDinhKem;
    private Short loaiTicketId;
    private Long hopDongId;
    private Integer sanPhamId;
    private String mucDoUuTien;
    private String nguonTiepNhan;
    private String trangThai;
    private Integer nhanVienTiepNhanId;
    private Integer nhanVienXuLyId;
    private LocalDateTime ngayHenXuLy;
    private LocalDateTime ngayDong;
    private String lyDoDong;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getFileDinhKem() { return fileDinhKem; }
    public void setFileDinhKem(String fileDinhKem) { this.fileDinhKem = fileDinhKem; }
    public Short getLoaiTicketId() { return loaiTicketId; }
    public void setLoaiTicketId(Short loaiTicketId) { this.loaiTicketId = loaiTicketId; }
    public Long getHopDongId() { return hopDongId; }
    public void setHopDongId(Long hopDongId) { this.hopDongId = hopDongId; }
    public Integer getSanPhamId() { return sanPhamId; }
    public void setSanPhamId(Integer sanPhamId) { this.sanPhamId = sanPhamId; }
    public String getMucDoUuTien() { return mucDoUuTien; }
    public void setMucDoUuTien(String mucDoUuTien) { this.mucDoUuTien = mucDoUuTien; }
    public String getNguonTiepNhan() { return nguonTiepNhan; }
    public void setNguonTiepNhan(String nguonTiepNhan) { this.nguonTiepNhan = nguonTiepNhan; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public Integer getNhanVienTiepNhanId() { return nhanVienTiepNhanId; }
    public void setNhanVienTiepNhanId(Integer nhanVienTiepNhanId) { this.nhanVienTiepNhanId = nhanVienTiepNhanId; }
    public Integer getNhanVienXuLyId() { return nhanVienXuLyId; }
    public void setNhanVienXuLyId(Integer nhanVienXuLyId) { this.nhanVienXuLyId = nhanVienXuLyId; }
    public LocalDateTime getNgayHenXuLy() { return ngayHenXuLy; }
    public void setNgayHenXuLy(LocalDateTime ngayHenXuLy) { this.ngayHenXuLy = ngayHenXuLy; }
    public LocalDateTime getNgayDong() { return ngayDong; }
    public void setNgayDong(LocalDateTime ngayDong) { this.ngayDong = ngayDong; }
    public String getLyDoDong() { return lyDoDong; }
    public void setLyDoDong(String lyDoDong) { this.lyDoDong = lyDoDong; }
}

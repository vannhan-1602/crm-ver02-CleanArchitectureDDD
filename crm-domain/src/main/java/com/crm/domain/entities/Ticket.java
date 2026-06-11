package com.crm.domain.entities;

import com.crm.domain.valueobjects.MucDoUuTienTicket;
import com.crm.domain.valueobjects.NguonTiepNhanTicket;
import com.crm.domain.valueobjects.TrangThaiTicket;

import java.time.LocalDateTime;

public class Ticket {

    private Long id;
    private String maTicket;
    private String tieuDe;
    private String moTa;
    private String fileDinhKem;
    private Short loaiTicketId;
    private Long khachHangId;
    private Long hopDongId;
    private Integer sanPhamId;
    private MucDoUuTienTicket mucDoUuTien;
    private NguonTiepNhanTicket nguonTiepNhan;
    private TrangThaiTicket trangThai;
    private Integer nhanVienTiepNhanId;
    private Integer nhanVienXuLyId;
    private LocalDateTime ngayHenXuLy;
    private LocalDateTime ngayDong;
    private String lyDoDong;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Ticket(String maTicket, String tieuDe, String moTa, String fileDinhKem,
                  Short loaiTicketId, Long khachHangId, Long hopDongId, Integer sanPhamId,
                  MucDoUuTienTicket mucDoUuTien, NguonTiepNhanTicket nguonTiepNhan,
                  TrangThaiTicket trangThai, Integer nhanVienTiepNhanId, Integer nhanVienXuLyId,
                  LocalDateTime ngayHenXuLy, LocalDateTime ngayDong, String lyDoDong) {
        this(null, maTicket, tieuDe, moTa, fileDinhKem, loaiTicketId, khachHangId, hopDongId,
                sanPhamId, mucDoUuTien, nguonTiepNhan, trangThai, nhanVienTiepNhanId,
                nhanVienXuLyId, ngayHenXuLy, ngayDong, lyDoDong, false, null, null);
    }

    public Ticket(Long id, String maTicket, String tieuDe, String moTa, String fileDinhKem,
                  Short loaiTicketId, Long khachHangId, Long hopDongId, Integer sanPhamId,
                  MucDoUuTienTicket mucDoUuTien, NguonTiepNhanTicket nguonTiepNhan,
                  TrangThaiTicket trangThai, Integer nhanVienTiepNhanId, Integer nhanVienXuLyId,
                  LocalDateTime ngayHenXuLy, LocalDateTime ngayDong, String lyDoDong,
                  Boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (maTicket == null || maTicket.isBlank()) {
            throw new IllegalArgumentException("MaTicket khong duoc de trong");
        }
        if (tieuDe == null || tieuDe.isBlank()) {
            throw new IllegalArgumentException("TieuDe khong duoc de trong");
        }
        if (khachHangId == null) {
            throw new IllegalArgumentException("KhachHang_Id khong duoc de trong");
        }
        this.id = id;
        this.maTicket = maTicket.trim();
        this.tieuDe = tieuDe.trim();
        this.moTa = moTa;
        this.fileDinhKem = fileDinhKem;
        this.loaiTicketId = loaiTicketId;
        this.khachHangId = khachHangId;
        this.hopDongId = hopDongId;
        this.sanPhamId = sanPhamId;
        this.mucDoUuTien = mucDoUuTien != null ? mucDoUuTien : MucDoUuTienTicket.TrungBinh;
        this.nguonTiepNhan = nguonTiepNhan != null ? nguonTiepNhan : NguonTiepNhanTicket.Phone;
        this.trangThai = trangThai != null ? trangThai : TrangThaiTicket.Moi;
        this.nhanVienTiepNhanId = nhanVienTiepNhanId;
        this.nhanVienXuLyId = nhanVienXuLyId;
        this.ngayHenXuLy = ngayHenXuLy;
        this.ngayDong = ngayDong;
        this.lyDoDong = lyDoDong;
        this.isDeleted = isDeleted != null ? isDeleted : false;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void capNhat(String tieuDe, String moTa, String fileDinhKem, Short loaiTicketId,
                        Long hopDongId, Integer sanPhamId, MucDoUuTienTicket mucDoUuTien,
                        NguonTiepNhanTicket nguonTiepNhan, TrangThaiTicket trangThai,
                        Integer nhanVienTiepNhanId, Integer nhanVienXuLyId,
                        LocalDateTime ngayHenXuLy, LocalDateTime ngayDong, String lyDoDong) {
        if (tieuDe != null && !tieuDe.isBlank()) this.tieuDe = tieuDe.trim();
        if (moTa != null) this.moTa = moTa;
        if (fileDinhKem != null) this.fileDinhKem = fileDinhKem;
        if (loaiTicketId != null) this.loaiTicketId = loaiTicketId;
        if (hopDongId != null) this.hopDongId = hopDongId;
        if (sanPhamId != null) this.sanPhamId = sanPhamId;
        if (mucDoUuTien != null) this.mucDoUuTien = mucDoUuTien;
        if (nguonTiepNhan != null) this.nguonTiepNhan = nguonTiepNhan;
        if (trangThai != null) this.trangThai = trangThai;
        if (nhanVienTiepNhanId != null) this.nhanVienTiepNhanId = nhanVienTiepNhanId;
        if (nhanVienXuLyId != null) this.nhanVienXuLyId = nhanVienXuLyId;
        if (ngayHenXuLy != null) this.ngayHenXuLy = ngayHenXuLy;
        if (ngayDong != null) this.ngayDong = ngayDong;
        if (lyDoDong != null) this.lyDoDong = lyDoDong;
    }

    public void doiTrangThai(TrangThaiTicket trangThai, LocalDateTime ngayDong, String lyDoDong) {
        if (trangThai == null) return;
        this.trangThai = trangThai;
        if (trangThai == TrangThaiTicket.Dong) {
            this.ngayDong = ngayDong != null ? ngayDong : LocalDateTime.now();
            this.lyDoDong = lyDoDong;
        }
    }

    public void xoaMem() { this.isDeleted = true; }

    public Long getId() { return id; }
    public String getMaTicket() { return maTicket; }
    public String getTieuDe() { return tieuDe; }
    public String getMoTa() { return moTa; }
    public String getFileDinhKem() { return fileDinhKem; }
    public Short getLoaiTicketId() { return loaiTicketId; }
    public Long getKhachHangId() { return khachHangId; }
    public Long getHopDongId() { return hopDongId; }
    public Integer getSanPhamId() { return sanPhamId; }
    public MucDoUuTienTicket getMucDoUuTien() { return mucDoUuTien; }
    public NguonTiepNhanTicket getNguonTiepNhan() { return nguonTiepNhan; }
    public TrangThaiTicket getTrangThai() { return trangThai; }
    public Integer getNhanVienTiepNhanId() { return nhanVienTiepNhanId; }
    public Integer getNhanVienXuLyId() { return nhanVienXuLyId; }
    public LocalDateTime getNgayHenXuLy() { return ngayHenXuLy; }
    public LocalDateTime getNgayDong() { return ngayDong; }
    public String getLyDoDong() { return lyDoDong; }
    public Boolean getIsDeleted() { return isDeleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}

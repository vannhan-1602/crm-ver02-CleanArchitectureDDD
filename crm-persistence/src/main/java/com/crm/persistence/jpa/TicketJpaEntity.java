package com.crm.persistence.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "TK_Ticket")
public class TicketJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "MaTicket", nullable = false, length = 30, unique = true)
    private String maTicket;

    @Column(name = "TieuDe", nullable = false, length = 255)
    private String tieuDe;

    @Column(name = "MoTa", columnDefinition = "text")
    private String moTa;

    @Column(name = "FileDinhKem", length = 500)
    private String fileDinhKem;

    @Column(name = "LoaiTicket_Id")
    private Short loaiTicketId;

    @Column(name = "KhachHang_Id", nullable = false)
    private Long khachHangId;

    @Column(name = "HopDong_Id")
    private Long hopDongId;

    @Column(name = "SanPham_Id")
    private Integer sanPhamId;

    @Column(name = "MucDoUuTien", length = 20)
    private String mucDoUuTien;

    @Column(name = "NguonTiepNhan", length = 20)
    private String nguonTiepNhan;

    @Column(name = "TrangThai", length = 20)
    private String trangThai;

    @Column(name = "NhanVienTiepNhan_Id")
    private Integer nhanVienTiepNhanId;

    @Column(name = "NhanVienXuLy_Id")
    private Integer nhanVienXuLyId;

    @Column(name = "NgayHenXuLy")
    private LocalDateTime ngayHenXuLy;

    @Column(name = "NgayDong")
    private LocalDateTime ngayDong;

    @Column(name = "LyDoDong", length = 500)
    private String lyDoDong;

    @Column(name = "IsDeleted")
    private Boolean isDeleted;

    @Column(name = "CreatedAt", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaTicket() { return maTicket; }
    public void setMaTicket(String maTicket) { this.maTicket = maTicket; }
    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getFileDinhKem() { return fileDinhKem; }
    public void setFileDinhKem(String fileDinhKem) { this.fileDinhKem = fileDinhKem; }
    public Short getLoaiTicketId() { return loaiTicketId; }
    public void setLoaiTicketId(Short loaiTicketId) { this.loaiTicketId = loaiTicketId; }
    public Long getKhachHangId() { return khachHangId; }
    public void setKhachHangId(Long khachHangId) { this.khachHangId = khachHangId; }
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
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean deleted) { isDeleted = deleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

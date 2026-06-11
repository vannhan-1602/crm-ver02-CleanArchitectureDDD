package com.crm.persistence.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "TK_Ticket_PhanHoi")
public class TicketPhanHoiJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Ticket_Id", nullable = false)
    private Long ticketId;

    @Column(name = "NguoiPhanHoi_Id")
    private Integer nguoiPhanHoiId;

    @Column(name = "LoaiPhanHoi", nullable = false, length = 30)
    private String loaiPhanHoi;

    @Column(name = "NoiDung", nullable = false, columnDefinition = "text")
    private String noiDung;

    @Column(name = "FileDinhKem", length = 500)
    private String fileDinhKem;

    @Column(name = "TrangThaiTruoc", length = 20)
    private String trangThaiTruoc;

    @Column(name = "TrangThaiSau", length = 20)
    private String trangThaiSau;

    @Column(name = "CreatedAt", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getTrangThaiTruoc() { return trangThaiTruoc; }
    public void setTrangThaiTruoc(String trangThaiTruoc) { this.trangThaiTruoc = trangThaiTruoc; }
    public String getTrangThaiSau() { return trangThaiSau; }
    public void setTrangThaiSau(String trangThaiSau) { this.trangThaiSau = trangThaiSau; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

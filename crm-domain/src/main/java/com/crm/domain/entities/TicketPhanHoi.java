package com.crm.domain.entities;

import com.crm.domain.valueobjects.LoaiPhanHoiTicket;
import com.crm.domain.valueobjects.TrangThaiTicket;

import java.time.LocalDateTime;

public class TicketPhanHoi {

    private Long id;
    private Long ticketId;
    private Integer nguoiPhanHoiId;
    private LoaiPhanHoiTicket loaiPhanHoi;
    private String noiDung;
    private String fileDinhKem;
    private TrangThaiTicket trangThaiTruoc;
    private TrangThaiTicket trangThaiSau;
    private LocalDateTime createdAt;

    public TicketPhanHoi(Long ticketId, Integer nguoiPhanHoiId, LoaiPhanHoiTicket loaiPhanHoi,
                         String noiDung, String fileDinhKem, TrangThaiTicket trangThaiTruoc,
                         TrangThaiTicket trangThaiSau) {
        this(null, ticketId, nguoiPhanHoiId, loaiPhanHoi, noiDung, fileDinhKem,
                trangThaiTruoc, trangThaiSau, null);
    }

    public TicketPhanHoi(Long id, Long ticketId, Integer nguoiPhanHoiId, LoaiPhanHoiTicket loaiPhanHoi,
                         String noiDung, String fileDinhKem, TrangThaiTicket trangThaiTruoc,
                         TrangThaiTicket trangThaiSau, LocalDateTime createdAt) {
        if (ticketId == null) {
            throw new IllegalArgumentException("Ticket_Id khong duoc de trong");
        }
        if (loaiPhanHoi == null) {
            throw new IllegalArgumentException("LoaiPhanHoi khong duoc de trong");
        }
        if (noiDung == null || noiDung.isBlank()) {
            throw new IllegalArgumentException("NoiDung khong duoc de trong");
        }
        this.id = id;
        this.ticketId = ticketId;
        this.nguoiPhanHoiId = nguoiPhanHoiId;
        this.loaiPhanHoi = loaiPhanHoi;
        this.noiDung = noiDung.trim();
        this.fileDinhKem = fileDinhKem;
        this.trangThaiTruoc = trangThaiTruoc;
        this.trangThaiSau = trangThaiSau;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getTicketId() { return ticketId; }
    public Integer getNguoiPhanHoiId() { return nguoiPhanHoiId; }
    public LoaiPhanHoiTicket getLoaiPhanHoi() { return loaiPhanHoi; }
    public String getNoiDung() { return noiDung; }
    public String getFileDinhKem() { return fileDinhKem; }
    public TrangThaiTicket getTrangThaiTruoc() { return trangThaiTruoc; }
    public TrangThaiTicket getTrangThaiSau() { return trangThaiSau; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

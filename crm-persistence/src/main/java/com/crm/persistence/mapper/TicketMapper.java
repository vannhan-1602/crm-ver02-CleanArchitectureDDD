package com.crm.persistence.mapper;

import com.crm.domain.entities.Ticket;
import com.crm.domain.valueobjects.MucDoUuTienTicket;
import com.crm.domain.valueobjects.NguonTiepNhanTicket;
import com.crm.domain.valueobjects.TrangThaiTicket;
import com.crm.persistence.jpa.TicketJpaEntity;

public final class TicketMapper {
    private TicketMapper() {}

    public static Ticket toDomain(TicketJpaEntity jpa) {
        return new Ticket(
                jpa.getId(),
                jpa.getMaTicket(),
                jpa.getTieuDe(),
                jpa.getMoTa(),
                jpa.getFileDinhKem(),
                jpa.getLoaiTicketId(),
                jpa.getKhachHangId(),
                jpa.getHopDongId(),
                jpa.getSanPhamId(),
                MucDoUuTienTicket.from(jpa.getMucDoUuTien()),
                NguonTiepNhanTicket.from(jpa.getNguonTiepNhan()),
                TrangThaiTicket.from(jpa.getTrangThai()),
                jpa.getNhanVienTiepNhanId(),
                jpa.getNhanVienXuLyId(),
                jpa.getNgayHenXuLy(),
                jpa.getNgayDong(),
                jpa.getLyDoDong(),
                jpa.getIsDeleted(),
                jpa.getCreatedAt(),
                jpa.getUpdatedAt()
        );
    }

    public static TicketJpaEntity toJpa(Ticket domain) {
        TicketJpaEntity jpa = new TicketJpaEntity();
        jpa.setId(domain.getId());
        jpa.setMaTicket(domain.getMaTicket());
        jpa.setTieuDe(domain.getTieuDe());
        jpa.setMoTa(domain.getMoTa());
        jpa.setFileDinhKem(domain.getFileDinhKem());
        jpa.setLoaiTicketId(domain.getLoaiTicketId());
        jpa.setKhachHangId(domain.getKhachHangId());
        jpa.setHopDongId(domain.getHopDongId());
        jpa.setSanPhamId(domain.getSanPhamId());
        jpa.setMucDoUuTien(domain.getMucDoUuTien() != null ? domain.getMucDoUuTien().name() : null);
        jpa.setNguonTiepNhan(domain.getNguonTiepNhan() != null ? domain.getNguonTiepNhan().name() : null);
        jpa.setTrangThai(domain.getTrangThai() != null ? domain.getTrangThai().name() : null);
        jpa.setNhanVienTiepNhanId(domain.getNhanVienTiepNhanId());
        jpa.setNhanVienXuLyId(domain.getNhanVienXuLyId());
        jpa.setNgayHenXuLy(domain.getNgayHenXuLy());
        jpa.setNgayDong(domain.getNgayDong());
        jpa.setLyDoDong(domain.getLyDoDong());
        jpa.setIsDeleted(domain.getIsDeleted());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        return jpa;
    }
}

package com.crm.persistence.mapper;

import com.crm.domain.entities.TicketPhanHoi;
import com.crm.domain.valueobjects.LoaiPhanHoiTicket;
import com.crm.domain.valueobjects.TrangThaiTicket;
import com.crm.persistence.jpa.TicketPhanHoiJpaEntity;

public final class TicketPhanHoiMapper {
    private TicketPhanHoiMapper() {}

    public static TicketPhanHoi toDomain(TicketPhanHoiJpaEntity jpa) {
        return new TicketPhanHoi(
                jpa.getId(),
                jpa.getTicketId(),
                jpa.getNguoiPhanHoiId(),
                LoaiPhanHoiTicket.from(jpa.getLoaiPhanHoi()),
                jpa.getNoiDung(),
                jpa.getFileDinhKem(),
                jpa.getTrangThaiTruoc() != null ? TrangThaiTicket.from(jpa.getTrangThaiTruoc()) : null,
                jpa.getTrangThaiSau() != null ? TrangThaiTicket.from(jpa.getTrangThaiSau()) : null,
                jpa.getCreatedAt()
        );
    }

    public static TicketPhanHoiJpaEntity toJpa(TicketPhanHoi domain) {
        TicketPhanHoiJpaEntity jpa = new TicketPhanHoiJpaEntity();
        jpa.setId(domain.getId());
        jpa.setTicketId(domain.getTicketId());
        jpa.setNguoiPhanHoiId(domain.getNguoiPhanHoiId());
        jpa.setLoaiPhanHoi(domain.getLoaiPhanHoi() != null ? domain.getLoaiPhanHoi().name() : null);
        jpa.setNoiDung(domain.getNoiDung());
        jpa.setFileDinhKem(domain.getFileDinhKem());
        jpa.setTrangThaiTruoc(domain.getTrangThaiTruoc() != null ? domain.getTrangThaiTruoc().name() : null);
        jpa.setTrangThaiSau(domain.getTrangThaiSau() != null ? domain.getTrangThaiSau().name() : null);
        jpa.setCreatedAt(domain.getCreatedAt());
        return jpa;
    }
}

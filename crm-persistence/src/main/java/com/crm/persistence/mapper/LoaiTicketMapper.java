package com.crm.persistence.mapper;

import com.crm.domain.entities.LoaiTicket;
import com.crm.persistence.jpa.LoaiTicketJpaEntity;

public final class LoaiTicketMapper {
    private LoaiTicketMapper() {}

    public static LoaiTicket toDomain(LoaiTicketJpaEntity jpa) {
        return new LoaiTicket(jpa.getId(), jpa.getTenLoai(), jpa.getMoTa(), jpa.getIsActive());
    }

    public static LoaiTicketJpaEntity toJpa(LoaiTicket domain) {
        LoaiTicketJpaEntity jpa = new LoaiTicketJpaEntity();
        jpa.setId(domain.getId());
        jpa.setTenLoai(domain.getTenLoai());
        jpa.setMoTa(domain.getMoTa());
        jpa.setIsActive(domain.getIsActive());
        return jpa;
    }
}

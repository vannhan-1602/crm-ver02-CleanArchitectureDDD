package com.crm.persistence.mapper;

import com.crm.domain.entities.Lead;
import com.crm.domain.valueobjects.TinhTrangLead;
import com.crm.persistence.jpa.LeadJpaEntity;


public final class LeadMapper {

    private LeadMapper() {}


    public static Lead toDomain(LeadJpaEntity jpa) {
        return new Lead(
                jpa.getId(),
                jpa.getTenLead(),
                jpa.getTenCongTy(),
                jpa.getSoDienThoai(),
                jpa.getEmail(),
                TinhTrangLead.from(jpa.getTinhTrang()),
                jpa.getNhanVienPhuTrachId(),
                jpa.isDeleted(),
                jpa.getCreatedAt(),
                jpa.getUpdatedAt()
        );
    }


    public static LeadJpaEntity toJpa(Lead domain) {
        LeadJpaEntity jpa = new LeadJpaEntity();
        jpa.setId(domain.getId());
        jpa.setTenLead(domain.getTenLead());
        jpa.setTenCongTy(domain.getTenCongTy());
        jpa.setSoDienThoai(domain.getSoDienThoai());
        jpa.setEmail(domain.getEmail());

        jpa.setTinhTrang(domain.getTinhTrang() != null
                ? domain.getTinhTrang().name() : TinhTrangLead.Moi.name());
        jpa.setNhanVienPhuTrachId(domain.getNhanVienPhuTrachId());
        jpa.setDeleted(domain.isDeleted());
       
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        return jpa;
    }
}
package com.crm.persistence.mapper;

import com.crm.domain.entities.HoatDong;
import com.crm.domain.valueobjects.LoaiHoatDong;
import com.crm.persistence.jpa.HoatDongJpaEntity;


public final class HoatDongMapper {

    private HoatDongMapper() {}

    public static HoatDong toDomain(HoatDongJpaEntity jpa) {
        return new HoatDong(
                jpa.getId(),
                jpa.getKhachHangId(),
                jpa.getLeadId(),
                LoaiHoatDong.from(jpa.getLoaiHoatDong()),
                jpa.getNoiDung(),
                jpa.getThoiGianThucHien(),
                jpa.getNhanVienId(),
                jpa.getCreatedAt(),
                jpa.getUpdatedAt()
        );
    }

    public static HoatDongJpaEntity toJpa(HoatDong domain) {
        HoatDongJpaEntity jpa = new HoatDongJpaEntity();
        jpa.setId(domain.getId());
        jpa.setKhachHangId(domain.getKhachHangId());
        jpa.setLeadId(domain.getLeadId());
        jpa.setLoaiHoatDong(domain.getLoaiHoatDong() != null
                ? domain.getLoaiHoatDong().name() : null);
        jpa.setNoiDung(domain.getNoiDung());
        jpa.setThoiGianThucHien(domain.getThoiGianThucHien());
        jpa.setNhanVienId(domain.getNhanVienId());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        return jpa;
    }
}
package com.crm.persistence.mapper;

import com.crm.domain.entities.DiaChi;
import com.crm.persistence.jpa.DiaChiJpaEntity;

public final class DiaChiMapper {

    private DiaChiMapper() {}

    public static DiaChi toDomain(DiaChiJpaEntity jpa) {
        return new DiaChi(
                jpa.getId(),
                jpa.getKhachHangId(),
                jpa.getDiaChiChiTiet(),
                jpa.getTinhThanh(),
                jpa.getQuanHuyen(),
                jpa.getPhuongXa(),
                jpa.getLoaiDiaChi(),
                jpa.isDefault()
        );
    }
}
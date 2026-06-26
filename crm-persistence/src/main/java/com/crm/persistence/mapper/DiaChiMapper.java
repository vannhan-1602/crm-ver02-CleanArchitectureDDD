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

    public static DiaChiJpaEntity toJpa(DiaChi diaChi) {
        DiaChiJpaEntity jpa = new DiaChiJpaEntity();
        jpa.setId(diaChi.getId());
        jpa.setKhachHangId(diaChi.getKhachHangId());
        jpa.setDiaChiChiTiet(diaChi.getDiaChiChiTiet());
        jpa.setTinhThanh(diaChi.getTinhThanh());
        jpa.setQuanHuyen(diaChi.getQuanHuyen());
        jpa.setPhuongXa(diaChi.getPhuongXa());
        jpa.setLoaiDiaChi(diaChi.getLoaiDiaChi());
        jpa.setDefault(diaChi.isDefault());
        return jpa;
    }
}

package com.crm.persistence.mapper;

import com.crm.domain.entities.LoaiSanPham;
import com.crm.persistence.jpa.LoaiSanPhamJpaEntity;

public final class LoaiSanPhamMapper {
    private LoaiSanPhamMapper() {
    }

    public static LoaiSanPham toDomain(LoaiSanPhamJpaEntity jpa) {
        LoaiSanPham domain = new LoaiSanPham();
        domain.setId(jpa.getId());
        domain.setTenLoai(jpa.getTenLoai());
        domain.setMoTa(jpa.getMoTa());
        return domain;
    }

    public static LoaiSanPhamJpaEntity toJpa(LoaiSanPham domain) {
        LoaiSanPhamJpaEntity jpa = new LoaiSanPhamJpaEntity();
        jpa.setId(domain.getId());
        jpa.setTenLoai(domain.getTenLoai());
        jpa.setMoTa(domain.getMoTa());
        return jpa;
    }
}

package com.crm.persistence.mapper;

import com.crm.domain.entities.SanPhamHinhAnh;
import com.crm.persistence.jpa.SanPhamHinhAnhJpaEntity;
import com.crm.persistence.jpa.SanPhamJpaEntity;

public final class SanPhamHinhAnhMapper {
    public SanPhamHinhAnhMapper() {
    }
    public static SanPhamHinhAnh toDomain(SanPhamHinhAnhJpaEntity jpa)
    {
        SanPhamHinhAnh domain= new SanPhamHinhAnh();
        domain.setId(jpa.getId());
        if (jpa.getSanPham() != null) {
            domain.setSanPham_Id(jpa.getSanPham().getId());
        }
        domain.setUrl(jpa.getUrl());
        domain.setIsMain(jpa.getIsMain());
        return domain;
    }
    public static SanPhamHinhAnhJpaEntity toJpa(SanPhamHinhAnh domain)
    {
        SanPhamHinhAnhJpaEntity jpa= new SanPhamHinhAnhJpaEntity();
        jpa.setId(domain.getId());
        jpa.setUrl(domain.getUrl());
        jpa.setIsMain(domain.getIsMain());
        if (domain.getSanPham_Id() != null) {
            SanPhamJpaEntity sanPham = new SanPhamJpaEntity();
            sanPham.setId(domain.getSanPham_Id());
            jpa.setSanPham(sanPham);
        }
        return jpa;
    }
}

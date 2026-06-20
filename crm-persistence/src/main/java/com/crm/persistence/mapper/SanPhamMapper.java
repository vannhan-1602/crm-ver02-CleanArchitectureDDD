package com.crm.persistence.mapper;

import com.crm.domain.entities.SanPham;
import com.crm.persistence.jpa.SanPhamHinhAnhJpaEntity;
import com.crm.persistence.jpa.SanPhamJpaEntity;

import java.util.ArrayList;
import java.util.List;

public final class SanPhamMapper {
    private SanPhamMapper() {
    }

    public static SanPham toDomain(SanPhamJpaEntity jpa) {
        SanPham domain = new SanPham();
        domain.setSanPhamId(jpa.getId());
        domain.setLoaiSanPham(jpa.getLoaiSanPham());
        domain.setMaSanPham(jpa.getMaSanPham());
        domain.setTenSanPham(jpa.getTenSanPham());
        domain.setDonVi(jpa.getDonVi());
        domain.setGiaBan(jpa.getGiaBan());
        domain.setSlTon(jpa.getSlTon());
        domain.setTrangThai(jpa.getTrangThai());
        domain.setCreatedAt(jpa.getCreatedAt());
        domain.setUpdatedAt(jpa.getUpdatedAt());
        if (jpa.getDshinhanh() != null) {
            domain.setHinhAnh(new ArrayList<>(jpa.getDshinhanh().stream()
                    .map(SanPhamHinhAnhMapper::toDomain)
                    .toList()));
        }
        return domain;
    }

    public static SanPhamJpaEntity toJpa(SanPham domain) {
        SanPhamJpaEntity jpa = new SanPhamJpaEntity();
        jpa.setId(domain.getSanPhamId());
        jpa.setLoaiSanPham(domain.getLoaiSanPham());
        jpa.setMaSanPham(domain.getMaSanPham());
        jpa.setTenSanPham(domain.getTenSanPham());
        jpa.setDonVi(domain.getDonVi());
        jpa.setGiaBan(domain.getGiaBan());
        jpa.setSlTon(domain.getSlTon());
        jpa.setTrangThai(domain.getTrangThai());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        if (domain.getHinhAnh() != null) {
            List<SanPhamHinhAnhJpaEntity> hinhAnh = domain.getHinhAnh().stream()
                    .map(SanPhamHinhAnhMapper::toJpa)
                    .toList();
            hinhAnh.forEach(item -> item.setSanPham(jpa));
            jpa.setDshinhanh(hinhAnh);
        }
        return jpa;
    }
}

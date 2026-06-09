package com.crm.persistence.mapper;

import com.crm.domain.entities.KhachHang;
import com.crm.persistence.jpa.KhachHangJpaEntity;


public final class KhachHangMapper {

    private KhachHangMapper() {}


    public static KhachHang toDomain(KhachHangJpaEntity jpa) {
        return new KhachHang(
                jpa.getId(),
                jpa.getMaKhachHang(),
                jpa.getTenKhachHang(),
                jpa.getEmail(),
                jpa.getSoDienThoai(),
                jpa.getNhanVienPhuTrachId(),
                jpa.isDeleted(),
                jpa.getCreatedAt(),
                jpa.getUpdatedAt()
        );
    }


    public static KhachHangJpaEntity toJpa(KhachHang domain) {
        KhachHangJpaEntity jpa = new KhachHangJpaEntity();
        jpa.setId(domain.getId());
        jpa.setMaKhachHang(domain.getMaKhachHang());
        jpa.setTenKhachHang(domain.getTenKhachHang());
        jpa.setEmail(domain.getEmail());
        jpa.setSoDienThoai(domain.getSoDienThoai());
        jpa.setNhanVienPhuTrachId(domain.getNhanVienPhuTrachId());
        jpa.setDeleted(domain.isDeleted());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        return jpa;
    }
}
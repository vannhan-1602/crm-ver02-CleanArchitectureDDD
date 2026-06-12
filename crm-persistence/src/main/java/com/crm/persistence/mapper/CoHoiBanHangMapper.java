package com.crm.persistence.mapper;

import com.crm.domain.entities.CoHoiBanHang;
import com.crm.persistence.jpa.CoHoiBanHangJpaEntity;

public class CoHoiBanHangMapper {
    private CoHoiBanHangMapper(){};
    public static CoHoiBanHang toDomain(CoHoiBanHangJpaEntity jpa)
    {
        CoHoiBanHang domain= new CoHoiBanHang();
        domain.setId(jpa.getId());
        domain.setTenThuongVu(jpa.getTenThuongVu());
        domain.setGiaiDoan(jpa.getGiaiDoan());
        domain.setKhachHang_Id(jpa.getKhachHang_Id());
        domain.setLead_Id(jpa.getLead_Id());
        domain.setTyLeThanhCong(jpa.getTyLeThanhCong());
        domain.setDoanhThuKyVong(jpa.getDoanhThuKyVong());
        domain.setGhiChu(jpa.getGhiChu());
        domain.setNgayDuKien(jpa.getNgayDuKien());
        domain.setNhanVienPhuTrach_Id(jpa.getNhanVienPhuTrach_Id());
        domain.setIsDeleted(jpa.getIsDeleted());
        domain.setCreatedAt(jpa.getCreatedAt());
        domain.setUpdatedAt(jpa.getUpdatedAt());
        return domain;
    }
    public static CoHoiBanHangJpaEntity toJpa(CoHoiBanHang domain)
    {
        CoHoiBanHangJpaEntity jpa=new CoHoiBanHangJpaEntity();
        jpa.setId(domain.getId());
        jpa.setTenThuongVu(domain.getTenThuongVu());
        jpa.setGiaiDoan(domain.getGiaiDoan());
        jpa.setKhachHang_Id(domain.getKhachHang_Id());
        jpa.setLead_Id(domain.getLead_Id());
        jpa.setTyLeThanhCong(domain.getTyLeThanhCong());
        jpa.setDoanhThuKyVong(domain.getDoanhThuKyVong());
        jpa.setGhiChu(domain.getGhiChu());
        jpa.setNgayDuKien(domain.getNgayDuKien());
        jpa.setNhanVienPhuTrach_Id(domain.getNhanVienPhuTrach_Id());
        jpa.setIsDeleted(domain.getIsDeleted());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        return jpa;
    }
}

package com.crm.persistence.repositories;

import com.crm.domain.valueobjects.LoaiPhieuThuChi;
import com.crm.persistence.entities.PhieuThuChiJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhieuThuChiEntityRepository extends JpaRepository<PhieuThuChiJpaEntity, Long> {
    List<PhieuThuChiJpaEntity> findByLoaiPhieu(LoaiPhieuThuChi loaiPhieu);
    List<PhieuThuChiJpaEntity> findByHoaDonIdAndLoaiPhieu(Long hoaDonId, LoaiPhieuThuChi loaiPhieu);
}

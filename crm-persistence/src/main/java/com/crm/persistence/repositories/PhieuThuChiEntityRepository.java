package com.crm.persistence.repositories;

import com.crm.domain.valueobjects.LoaiPhieuThuChi;
import com.crm.persistence.entities.PhieuThuChiJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhieuThuChiEntityRepository extends JpaRepository<PhieuThuChiJpaEntity, Long> {
    List<PhieuThuChiJpaEntity> findByLoaiPhieu(LoaiPhieuThuChi loaiPhieu);
    List<PhieuThuChiJpaEntity> findByHoaDonIdAndLoaiPhieu(Long hoaDonId, LoaiPhieuThuChi loaiPhieu);

    @Query("""
            SELECT p FROM PhieuThuChiJpaEntity p
            WHERE p.loaiPhieu = :loaiPhieu
              AND (:from IS NULL OR p.ngayTao >= :from)
              AND (:to IS NULL OR p.ngayTao <= :to)
            """)
    List<PhieuThuChiJpaEntity> findByLoaiPhieuAndNgayTaoRange(
            @Param("loaiPhieu") LoaiPhieuThuChi loaiPhieu,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}

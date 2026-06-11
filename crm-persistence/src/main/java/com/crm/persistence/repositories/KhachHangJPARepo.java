package com.crm.persistence.repositories;

import com.crm.persistence.jpa.KhachHangJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface KhachHangJPARepo extends JpaRepository<KhachHangJpaEntity, Long> {

    List<KhachHangJpaEntity> findByIsDeletedFalse();

    Optional<KhachHangJpaEntity> findByIdAndIsDeletedFalse(Long id);

    List<KhachHangJpaEntity> findByLoaiKhachHangIdAndIsDeletedFalse(Integer loaiKhachHangId);

    @Query("SELECT k.maKhachHang FROM KhachHangJpaEntity k " +
            "WHERE k.maKhachHang LIKE 'KH%' " +
            "ORDER BY k.maKhachHang DESC LIMIT 1")
    Optional<String> findMaxMaKhachHang();

    @Modifying
    @Transactional
    @Query("UPDATE KhachHangJpaEntity k SET k.isDeleted = true WHERE k.id = :id")
    void softDeleteById(@Param("id") Long id);
}
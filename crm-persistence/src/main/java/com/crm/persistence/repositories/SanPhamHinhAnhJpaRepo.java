package com.crm.persistence.repositories;

import com.crm.persistence.jpa.SanPhamHinhAnhJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamHinhAnhJpaRepo extends JpaRepository<SanPhamHinhAnhJpaEntity,Integer> {
    @Query("select h from SanPhamHinhAnhJpaEntity h where h.sanPham.id = :sanPhamId")
    List<SanPhamHinhAnhJpaEntity> findBySanPhamId(@Param("sanPhamId") Integer sanPhamId);
}

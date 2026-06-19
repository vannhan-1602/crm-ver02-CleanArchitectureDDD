package com.crm.persistence.repositories;

import com.crm.persistence.jpa.SanPhamJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamJPARepo extends JpaRepository<SanPhamJpaEntity, Integer> {
    @Override
    @EntityGraph(attributePaths = {"dshinhanh"})
    List<SanPhamJpaEntity> findAll();
    @Override
    @EntityGraph(attributePaths = {"dshinhanh"})
    Optional<SanPhamJpaEntity> findById(Integer id);
    @Query("SELECT s FROM SanPhamJpaEntity s LEFT JOIN FETCH s.dshinhanh")
    List<SanPhamJpaEntity> findAllWithHinhAnh();
}

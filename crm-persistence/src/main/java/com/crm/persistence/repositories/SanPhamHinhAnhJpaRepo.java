package com.crm.persistence.repositories;

import com.crm.persistence.jpa.SanPhamHinhAnhJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamHinhAnhJpaRepo extends JpaRepository<SanPhamHinhAnhJpaEntity,Integer> {
}

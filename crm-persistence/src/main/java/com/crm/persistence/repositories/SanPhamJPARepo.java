package com.crm.persistence.repositories;

import com.crm.persistence.jpa.SanPhamJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamJPARepo extends JpaRepository<SanPhamJpaEntity, Integer> {
}

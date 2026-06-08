package com.crm.persistence.repositories;

import com.crm.persistence.jpa.LoaiSanPhamJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiSanPhamJPArepo extends JpaRepository<LoaiSanPhamJpaEntity, Integer> {
}

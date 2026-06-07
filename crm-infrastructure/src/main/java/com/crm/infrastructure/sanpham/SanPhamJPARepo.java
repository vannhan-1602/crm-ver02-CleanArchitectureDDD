package com.crm.infrastructure.sanpham;


import com.crm.domain.entities.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SanPhamJPARepo extends JpaRepository<SanPham, Integer> {
}

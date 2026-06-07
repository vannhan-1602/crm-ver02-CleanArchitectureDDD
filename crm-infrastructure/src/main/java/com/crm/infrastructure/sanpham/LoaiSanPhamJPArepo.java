package com.crm.infrastructure.sanpham;



import com.crm.domain.entities.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface LoaiSanPhamJPArepo extends JpaRepository<LoaiSanPham, Integer> {
}

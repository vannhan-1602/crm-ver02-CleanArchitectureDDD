package com.crm.domain.repositories;


import com.crm.domain.entities.LoaiSanPham;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoaiSanPhamRepo {
    List<LoaiSanPham> findAll();
    Optional<LoaiSanPham> findById(Integer  id);
    LoaiSanPham save(LoaiSanPham sanPham);
    void Delete(LoaiSanPham sanPham);
}

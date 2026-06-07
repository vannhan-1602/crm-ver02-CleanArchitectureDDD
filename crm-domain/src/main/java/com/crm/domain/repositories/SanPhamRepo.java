package com.crm.domain.repositories;

import com.crm.domain.entities.SanPham;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SanPhamRepo {
    List<SanPham> findAll();
    Optional<SanPham> findById(Integer  id);
    SanPham save(SanPham sanPham);
    void update(Integer id,SanPham sanPham);
    void delete(SanPham sanPham);

}

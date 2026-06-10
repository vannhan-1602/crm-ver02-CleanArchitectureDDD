package com.crm.domain.repositories;

import com.crm.domain.entities.SanPhamHinhAnh;


import java.util.List;
import java.util.Optional;

public interface SanPhamHinhAnhRepo {
    List<SanPhamHinhAnh> findAll();
    Optional<SanPhamHinhAnh> findById(Integer  id);
    SanPhamHinhAnh save(SanPhamHinhAnh sanPham);
    void update(Integer id,SanPhamHinhAnh sanPham);
    void delete(SanPhamHinhAnh sanPham);
}

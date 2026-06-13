package com.crm.domain.repositories;

import com.crm.domain.entities.KhachHang;

import java.util.List;
import java.util.Optional;

public interface KhachHangRepo {

    KhachHang save(KhachHang khachHang);

    Optional<KhachHang> findById(Long id);

    Optional<KhachHang> findByIdIncludingDeleted(Long id);

    List<KhachHang> findAll();

    List<KhachHang> findByLoaiKhachHangId(Integer loaiKhachHangId);

    Optional<String> findMaxMaKhachHang();

    void softDeleteById(Long id);
    default Optional<String> findTenById(Integer id) {
        if (id == null) return Optional.empty();
        return findById(Long.valueOf(id))
                .map(KhachHang::getTenKhachHang);
    }
}
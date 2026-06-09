package com.crm.domain.repositories;

import com.crm.domain.entities.KhachHang;

import java.util.List;
import java.util.Optional;

public interface KhachHangRepo {

    KhachHang save(KhachHang khachHang);

    Optional<KhachHang> findById(Long id);

    List<KhachHang> findAll();

  
    Optional<String> findMaxMaKhachHang();

    void softDeleteById(Long id);
}
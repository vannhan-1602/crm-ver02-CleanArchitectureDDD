package com.crm.domain.repositories;

import com.crm.domain.entities.PhieuThu;

import java.util.List;
import java.util.Optional;

public interface PhieuThuRepo {
    PhieuThu save(PhieuThu phieuThu);
    Optional<PhieuThu> findById(Long id);
    List<PhieuThu> findAll();
    List<PhieuThu> findByHoaDonId(Long hoaDonId);
    void deleteById(Long id);
}

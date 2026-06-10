package com.crm.domain.repositories;

import com.crm.domain.entities.PhieuChi;

import java.util.List;
import java.util.Optional;

public interface PhieuChiRepo {
    PhieuChi save(PhieuChi phieuChi);
    Optional<PhieuChi> findById(Long id);
    List<PhieuChi> findAll();
    List<PhieuChi> findByHoaDonId(Long hoaDonId);
    void deleteById(Long id);
}

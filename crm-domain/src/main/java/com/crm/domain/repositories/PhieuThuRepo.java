package com.crm.domain.repositories;

import com.crm.domain.entities.PhieuThu;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PhieuThuRepo {
    PhieuThu save(PhieuThu phieuThu);
    Optional<PhieuThu> findById(Long id);
    List<PhieuThu> findAll();
    List<PhieuThu> findByHoaDonId(Long hoaDonId);
    List<PhieuThu> findByNgayTaoBetween(LocalDateTime from, LocalDateTime to);
    void deleteById(Long id);
}

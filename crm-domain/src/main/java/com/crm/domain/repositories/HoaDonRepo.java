package com.crm.domain.repositories;

import com.crm.domain.entities.HoaDon;

import java.util.List;
import java.util.Optional;

public interface HoaDonRepo {
    HoaDon save(HoaDon hoaDon);
    Optional<HoaDon> findById(Long id);
    List<HoaDon> findAll();
    void deleteById(Long id);
}

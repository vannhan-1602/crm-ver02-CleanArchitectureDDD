package com.crm.domain.repositories;

import com.crm.domain.entities.HopDong;

import java.util.List;
import java.util.Optional;

public interface HopDongRepo {
    HopDong save(HopDong hopDong);
    Optional<HopDong> findById(Long id);
    List<HopDong> findAll();
    void deleteById(Long id);
}

package com.crm.domain.repositories;

import com.crm.domain.entities.BaoGia;

import java.util.List;
import java.util.Optional;

public interface BaoGiaRepo {
    BaoGia save(BaoGia baoGia);

    Optional<BaoGia> findById(Long id);

    List<BaoGia> findAll();

    void deleteById(Long id);
}

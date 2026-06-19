package com.crm.domain.repositories;

import com.crm.domain.entities.CoHoiBanHang;

import java.util.List;
import java.util.Optional;

public interface CoiHoiBanHangRepo {
    List<CoHoiBanHang> findAll();
    Optional<CoHoiBanHang> findById(Integer  id);
    CoHoiBanHang save(CoHoiBanHang cohoi);
    CoHoiBanHang update(Integer id,CoHoiBanHang cohoi);
    void delete(CoHoiBanHang cohoi);
}

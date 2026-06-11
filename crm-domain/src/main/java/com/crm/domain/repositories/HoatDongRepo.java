package com.crm.domain.repositories;

import com.crm.domain.entities.HoatDong;

import java.util.List;
import java.util.Optional;

public interface HoatDongRepo {

    HoatDong save(HoatDong hoatDong);

    Optional<HoatDong> findById(Long id);

    List<HoatDong> findAll();

    List<HoatDong> findByKhachHangId(Long khachHangId);

    List<HoatDong> findByLeadId(Long leadId);

    void deleteById(Long id);
}
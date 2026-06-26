package com.crm.domain.repositories;

import com.crm.domain.entities.DiaChi;

import java.util.List;

public interface DiaChiRepo {
    List<DiaChi> findByKhachHangId(Long khachHangId);
    DiaChi save(DiaChi diaChi);
}

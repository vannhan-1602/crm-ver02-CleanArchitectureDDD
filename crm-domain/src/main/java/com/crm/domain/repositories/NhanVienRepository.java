package com.crm.domain.repositories;

import com.crm.domain.entities.NhanVien;

import java.util.List;
import java.util.Optional;

public interface NhanVienRepository {
    Optional<String> findHoTenById(Integer userId);
    List<NhanVien>  findAll();
}

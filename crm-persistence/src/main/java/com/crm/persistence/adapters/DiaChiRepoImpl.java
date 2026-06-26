package com.crm.persistence.adapters;

import com.crm.domain.entities.DiaChi;
import com.crm.domain.repositories.DiaChiRepo;
import com.crm.persistence.mapper.DiaChiMapper;
import com.crm.persistence.repositories.DiaChiJPARepo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DiaChiRepoImpl implements DiaChiRepo {

    private final DiaChiJPARepo diaChiJPARepo;

    public DiaChiRepoImpl(DiaChiJPARepo diaChiJPARepo) {
        this.diaChiJPARepo = diaChiJPARepo;
    }

    @Override
    public List<DiaChi> findByKhachHangId(Long khachHangId) {
        return diaChiJPARepo.findByKhachHangId(khachHangId)
                .stream()
                .map(DiaChiMapper::toDomain)
                .toList();
    }

    @Override
    public DiaChi save(DiaChi diaChi) {
        return DiaChiMapper.toDomain(diaChiJPARepo.save(DiaChiMapper.toJpa(diaChi)));
    }
}

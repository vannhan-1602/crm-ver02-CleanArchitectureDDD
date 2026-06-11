package com.crm.persistence.adapters;

import com.crm.domain.entities.HoatDong;
import com.crm.domain.repositories.HoatDongRepo;
import com.crm.persistence.mapper.HoatDongMapper;
import com.crm.persistence.repositories.HoatDongJPARepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class HoatDongJpaRepoImpl implements HoatDongRepo {

    private final HoatDongJPARepo jpaRepo;

    public HoatDongJpaRepoImpl(HoatDongJPARepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public HoatDong save(HoatDong hoatDong) {
        return HoatDongMapper.toDomain(jpaRepo.save(HoatDongMapper.toJpa(hoatDong)));
    }

    @Override
    public Optional<HoatDong> findById(Long id) {
        return jpaRepo.findById(id).map(HoatDongMapper::toDomain);
    }

    @Override
    public List<HoatDong> findAll() {
        return jpaRepo.findAll()
                .stream()
                .map(HoatDongMapper::toDomain)
                .toList();
    }

    @Override
    public List<HoatDong> findByKhachHangId(Long khachHangId) {
        return jpaRepo.findByKhachHangId(khachHangId)
                .stream()
                .map(HoatDongMapper::toDomain)
                .toList();
    }

    @Override
    public List<HoatDong> findByLeadId(Long leadId) {
        return jpaRepo.findByLeadId(leadId)
                .stream()
                .map(HoatDongMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }
}
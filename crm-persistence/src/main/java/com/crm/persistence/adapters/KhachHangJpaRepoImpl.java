package com.crm.persistence.adapters;

import com.crm.domain.entities.KhachHang;
import com.crm.domain.repositories.KhachHangRepo;
import com.crm.persistence.mapper.KhachHangMapper;
import com.crm.persistence.repositories.KhachHangJPARepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class KhachHangJpaRepoImpl implements KhachHangRepo {

    private final KhachHangJPARepo jpaRepo;

    public KhachHangJpaRepoImpl(KhachHangJPARepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public KhachHang save(KhachHang khachHang) {
        return KhachHangMapper.toDomain(jpaRepo.save(KhachHangMapper.toJpa(khachHang)));
    }

    @Override
    public Optional<KhachHang> findById(Long id) {
        return jpaRepo.findByIdAndIsDeletedFalse(id)
                .map(KhachHangMapper::toDomain);
    }

    @Override
    public Optional<KhachHang> findByIdIncludingDeleted(Long id) {
        return jpaRepo.findById(id)
                .map(KhachHangMapper::toDomain);
    }

    @Override
    public List<KhachHang> findAll() {
        return jpaRepo.findByIsDeletedFalse()
                .stream()
                .map(KhachHangMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<String> findMaxMaKhachHang() {
        return jpaRepo.findMaxMaKhachHang();
    }

    @Override
    public void softDeleteById(Long id) {
        jpaRepo.softDeleteById(id);
    }
}

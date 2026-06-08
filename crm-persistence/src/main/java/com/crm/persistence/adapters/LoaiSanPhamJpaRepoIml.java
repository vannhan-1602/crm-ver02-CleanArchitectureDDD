package com.crm.persistence.adapters;

import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.repositories.LoaiSanPhamRepo;
import com.crm.persistence.mapper.LoaiSanPhamMapper;
import com.crm.persistence.repositories.LoaiSanPhamJPArepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LoaiSanPhamJpaRepoIml implements LoaiSanPhamRepo {
    private final LoaiSanPhamJPArepo jpaRepo;

    public LoaiSanPhamJpaRepoIml(LoaiSanPhamJPArepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public List<LoaiSanPham> findAll() {
        return jpaRepo.findAll().stream()
                .map(LoaiSanPhamMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<LoaiSanPham> findById(Integer id) {
        return jpaRepo.findById(id).map(LoaiSanPhamMapper::toDomain);
    }

    @Override
    public LoaiSanPham save(LoaiSanPham loaiSanPham) {
        return LoaiSanPhamMapper.toDomain(jpaRepo.save(LoaiSanPhamMapper.toJpa(loaiSanPham)));
    }

    @Override
    public void delete(LoaiSanPham loaiSanPham) {
        jpaRepo.deleteById(loaiSanPham.getId());
    }
}

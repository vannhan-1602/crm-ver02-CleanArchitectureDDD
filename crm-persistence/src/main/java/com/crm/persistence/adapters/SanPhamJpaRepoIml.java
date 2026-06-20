package com.crm.persistence.adapters;

import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
import com.crm.persistence.mapper.SanPhamMapper;
import com.crm.persistence.repositories.SanPhamJPARepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SanPhamJpaRepoIml implements SanPhamRepo {
    private final SanPhamJPARepo jpaRepo;

    public SanPhamJpaRepoIml(SanPhamJPARepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public List<SanPham> findAll() {
        return jpaRepo.findAllWithHinhAnh().stream()
                .map(SanPhamMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Optional<SanPham> findById(Integer id) {
        return jpaRepo.findById(id).map(SanPhamMapper::toDomain);
    }

    @Override
    public SanPham save(SanPham sanPham) {
        return SanPhamMapper.toDomain(jpaRepo.save(SanPhamMapper.toJpa(sanPham)));
    }

    @Override
    public void delete(SanPham sanPham) {
        jpaRepo.deleteById(sanPham.getSanPhamId());
    }

    @Override
    public void update(Integer id, SanPham sanPham) {
        sanPham.setSanPhamId(id);
        save(sanPham);
    }
}

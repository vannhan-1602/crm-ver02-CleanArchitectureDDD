package com.crm.persistence.adapters;

import com.crm.domain.entities.SanPhamHinhAnh;
import com.crm.domain.repositories.SanPhamHinhAnhRepo;
import com.crm.persistence.mapper.SanPhamHinhAnhMapper;
import com.crm.persistence.mapper.SanPhamMapper;
import com.crm.persistence.repositories.SanPhamHinhAnhJpaRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class SanPhamHinhAnhJpaRepoIml implements SanPhamHinhAnhRepo {
    private final SanPhamHinhAnhJpaRepo repo;

    public SanPhamHinhAnhJpaRepoIml(SanPhamHinhAnhJpaRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<SanPhamHinhAnh> findAll() {
        return repo.findAll().stream().map(SanPhamHinhAnhMapper::toDomain).toList();
    }

    @Override
    public Optional<SanPhamHinhAnh> findById(Integer id) {
        return repo.findById(id).map(SanPhamHinhAnhMapper::toDomain);
    }

    @Override
    public SanPhamHinhAnh save(SanPhamHinhAnh sanPham) {
        return SanPhamHinhAnhMapper.toDomain(repo.save(SanPhamHinhAnhMapper.toJpa(sanPham)));
    }

    @Override
    public void update(Integer id, SanPhamHinhAnh sanPham) {
        sanPham.setId(id);
        save(sanPham);
    }

    @Override
        public void delete(SanPhamHinhAnh sanPham) {
            repo.deleteById(sanPham.getId());
        }
}

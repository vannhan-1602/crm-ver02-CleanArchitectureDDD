package com.crm.infrastructure.sanpham;

import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
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
        return jpaRepo.findAll();
    }

    @Override
    public Optional<SanPham> findById(Integer  id) {
        return jpaRepo.findById(id);
    }
    @Override
    public SanPham save(SanPham sanPham) {
        return jpaRepo.save(sanPham);
    }

    @Override
    public void delete(SanPham sanPham) {
        jpaRepo.delete(sanPham);
    }
    @Override
    public void update(Integer id, SanPham sanPham) {
        sanPham.setSanPhamId(id);   // đảm bảo có Id
        jpaRepo.save(sanPham);
    }
}

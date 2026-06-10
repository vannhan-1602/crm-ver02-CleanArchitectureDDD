package com.crm.persistence.adapters;

import com.crm.domain.entities.BaoGia;
import com.crm.domain.repositories.BaoGiaRepo;
import com.crm.persistence.mapper.BaoGiaMapper;
import com.crm.persistence.repositories.BaoGiaJPARepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BaoGiaJpaRepoImpl implements BaoGiaRepo {
    private final BaoGiaJPARepo jpaRepo;

    public BaoGiaJpaRepoImpl(BaoGiaJPARepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public BaoGia save(BaoGia baoGia) {
        return BaoGiaMapper.toDomain(jpaRepo.save(BaoGiaMapper.toJpa(baoGia)));
    }

    @Override
    public Optional<BaoGia> findById(Long id) {
        return jpaRepo.findById(id).map(BaoGiaMapper::toDomain);
    }

    @Override
    public List<BaoGia> findAll() {
        return jpaRepo.findAll().stream()
                .map(BaoGiaMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }
}

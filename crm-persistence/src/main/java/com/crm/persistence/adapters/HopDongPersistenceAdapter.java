package com.crm.persistence.adapters;

import com.crm.domain.entities.HopDong;
import com.crm.domain.repositories.HopDongRepo;
import com.crm.persistence.jpa.HopDongJpaEntity;
import com.crm.persistence.mapper.HopDongMapper;
import com.crm.persistence.repositories.HopDongEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class HopDongPersistenceAdapter implements HopDongRepo {
    private final HopDongEntityRepository entityRepository;

    public HopDongPersistenceAdapter(HopDongEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public HopDong save(HopDong hopDong) {
        HopDongJpaEntity saved = entityRepository.save(HopDongMapper.toJpa(hopDong));
        return HopDongMapper.toDomain(saved);
    }

    @Override
    public Optional<HopDong> findById(Long id) {
        return entityRepository.findById(id).map(HopDongMapper::toDomain);
    }

    @Override
    public List<HopDong> findAll() {
        return entityRepository.findAll()
                .stream()
                .map(HopDongMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        entityRepository.deleteById(id);
    }
}

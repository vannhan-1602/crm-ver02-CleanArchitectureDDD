package com.crm.persistence.repositories;

import com.crm.domain.entities.HopDong;
import com.crm.domain.repositories.HopDongRepo;
import com.crm.domain.valueobjects.MaHopDong;
import com.crm.persistence.entities.HopDongJpaEntity;
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
        HopDongJpaEntity saved = entityRepository.save(toEntity(hopDong));
        return toDomain(saved);
    }

    @Override
    public Optional<HopDong> findById(Long id) {
        return entityRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<HopDong> findAll() {
        return entityRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        entityRepository.deleteById(id);
    }

    private HopDong toDomain(HopDongJpaEntity entity) {
        return new HopDong(
                entity.getId(),
                new MaHopDong(entity.getMaHopDong()),
                entity.getKhachHangId(),
                entity.getNgayKy(),
                entity.getThoiHan(),
                entity.getTrangThai(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private HopDongJpaEntity toEntity(HopDong hopDong) {
        return new HopDongJpaEntity(
                hopDong.getId(),
                hopDong.getMaHopDong().getValue(),
                hopDong.getKhachHangId(),
                hopDong.getNgayKy(),
                hopDong.getThoiHan(),
                hopDong.getTrangThai(),
                hopDong.getCreatedAt(),
                hopDong.getUpdatedAt()
        );
    }
}

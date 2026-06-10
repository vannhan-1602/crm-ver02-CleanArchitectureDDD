package com.crm.persistence.repositories;

import com.crm.domain.entities.HoaDon;
import com.crm.domain.repositories.HoaDonRepo;
import com.crm.domain.valueobjects.MaHoaDon;
import com.crm.persistence.entities.HoaDonJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HoaDonPersistenceAdapter implements HoaDonRepo {
    private final HoaDonEntityRepository entityRepository;

    public HoaDonPersistenceAdapter(HoaDonEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public HoaDon save(HoaDon hoaDon) {
        HoaDonJpaEntity saved = entityRepository.save(toEntity(hoaDon));
        return toDomain(saved);
    }

    @Override
    public Optional<HoaDon> findById(Long id) {
        return entityRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<HoaDon> findAll() {
        return entityRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        entityRepository.deleteById(id);
    }

    private HoaDon toDomain(HoaDonJpaEntity entity) {
        return new HoaDon(
                entity.getId(),
                new MaHoaDon(entity.getMaHoaDon()),
                entity.getHopDongId(),
                entity.getKhachHangId(),
                entity.getTongTien(),
                entity.getSoTienDaThu(),
                entity.getTrangThaiThanhToan(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private HoaDonJpaEntity toEntity(HoaDon hoaDon) {
        return new HoaDonJpaEntity(
                hoaDon.getId(),
                hoaDon.getMaHoaDon().getValue(),
                hoaDon.getHopDongId(),
                hoaDon.getKhachHangId(),
                hoaDon.getTongTien(),
                hoaDon.getSoTienDaThu(),
                hoaDon.getTrangThaiThanhToan(),
                hoaDon.getCreatedAt(),
                hoaDon.getUpdatedAt()
        );
    }
}

package com.crm.persistence.repositories;

import com.crm.domain.entities.PhieuThu;
import com.crm.domain.repositories.PhieuThuRepo;
import com.crm.domain.valueobjects.LoaiPhieuThuChi;
import com.crm.domain.valueobjects.MaPhieuThu;
import com.crm.persistence.entities.PhieuThuChiJpaEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PhieuThuPersistenceAdapter implements PhieuThuRepo {
    private final PhieuThuChiEntityRepository entityRepository;

    public PhieuThuPersistenceAdapter(PhieuThuChiEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public PhieuThu save(PhieuThu phieuThu) {
        PhieuThuChiJpaEntity saved = entityRepository.save(toEntity(phieuThu));
        return toDomain(saved);
    }

    @Override
    public Optional<PhieuThu> findById(Long id) {
        return entityRepository.findById(id)
                .filter(entity -> entity.getLoaiPhieu() == LoaiPhieuThuChi.Thu)
                .map(this::toDomain);
    }

    @Override
    public List<PhieuThu> findAll() {
        return entityRepository.findByLoaiPhieu(LoaiPhieuThuChi.Thu)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<PhieuThu> findByHoaDonId(Long hoaDonId) {
        return entityRepository.findByHoaDonIdAndLoaiPhieu(hoaDonId, LoaiPhieuThuChi.Thu)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<PhieuThu> findByNgayTaoBetween(LocalDateTime from, LocalDateTime to) {
        return entityRepository.findByLoaiPhieuAndNgayTaoRange(LoaiPhieuThuChi.Thu, from, to)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        entityRepository.findById(id)
                .filter(entity -> entity.getLoaiPhieu() == LoaiPhieuThuChi.Thu)
                .ifPresent(entityRepository::delete);
    }

    private PhieuThu toDomain(PhieuThuChiJpaEntity entity) {
        return new PhieuThu(
                entity.getId(),
                new MaPhieuThu(entity.getMaPhieu()),
                entity.getKhachHangId(),
                entity.getHoaDonId(),
                entity.getSoTien(),
                entity.getNguoiLapId(),
                entity.getNgayTao(),
                entity.getUpdatedAt()
        );
    }

    private PhieuThuChiJpaEntity toEntity(PhieuThu phieuThu) {
        return new PhieuThuChiJpaEntity(
                phieuThu.getId(),
                phieuThu.getMaPhieuThu().getValue(),
                LoaiPhieuThuChi.Thu,
                phieuThu.getKhachHangId(),
                phieuThu.getHoaDonId(),
                phieuThu.getSoTien(),
                phieuThu.getNguoiLapId(),
                phieuThu.getNgayTao(),
                phieuThu.getUpdatedAt()
        );
    }
}

package com.crm.persistence.repositories;

import com.crm.domain.entities.PhieuChi;
import com.crm.domain.repositories.PhieuChiRepo;
import com.crm.domain.valueobjects.LoaiPhieuThuChi;
import com.crm.domain.valueobjects.MaPhieuChi;
import com.crm.persistence.entities.PhieuThuChiJpaEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PhieuChiPersistenceAdapter implements PhieuChiRepo {
    private final PhieuThuChiEntityRepository entityRepository;

    public PhieuChiPersistenceAdapter(PhieuThuChiEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public PhieuChi save(PhieuChi phieuChi) {
        PhieuThuChiJpaEntity saved = entityRepository.save(toEntity(phieuChi));
        return toDomain(saved);
    }

    @Override
    public Optional<PhieuChi> findById(Long id) {
        return entityRepository.findById(id)
                .filter(entity -> entity.getLoaiPhieu() == LoaiPhieuThuChi.Chi)
                .map(this::toDomain);
    }

    @Override
    public List<PhieuChi> findAll() {
        return entityRepository.findByLoaiPhieu(LoaiPhieuThuChi.Chi)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<PhieuChi> findByHoaDonId(Long hoaDonId) {
        return entityRepository.findByHoaDonIdAndLoaiPhieu(hoaDonId, LoaiPhieuThuChi.Chi)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<PhieuChi> findByNgayTaoBetween(LocalDateTime from, LocalDateTime to) {
        return entityRepository.findByLoaiPhieuAndNgayTaoRange(LoaiPhieuThuChi.Chi, from, to)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        entityRepository.findById(id)
                .filter(entity -> entity.getLoaiPhieu() == LoaiPhieuThuChi.Chi)
                .ifPresent(entityRepository::delete);
    }

    private PhieuChi toDomain(PhieuThuChiJpaEntity entity) {
        return new PhieuChi(
                entity.getId(),
                new MaPhieuChi(entity.getMaPhieu()),
                entity.getKhachHangId(),
                entity.getHoaDonId(),
                entity.getSoTien(),
                entity.getNguoiLapId(),
                entity.getNgayTao(),
                entity.getUpdatedAt()
        );
    }

    private PhieuThuChiJpaEntity toEntity(PhieuChi phieuChi) {
        return new PhieuThuChiJpaEntity(
                phieuChi.getId(),
                phieuChi.getMaPhieuChi().getValue(),
                LoaiPhieuThuChi.Chi,
                phieuChi.getKhachHangId(),
                phieuChi.getHoaDonId(),
                phieuChi.getSoTien(),
                phieuChi.getNguoiLapId(),
                phieuChi.getNgayTao(),
                phieuChi.getUpdatedAt()
        );
    }
}

package com.crm.persistence.adapters;

import com.crm.domain.entities.CoHoiBanHang;
import com.crm.domain.repositories.CoiHoiBanHangRepo;
import com.crm.persistence.jpa.CoHoiBanHangJpaEntity;
import com.crm.persistence.mapper.CoHoiBanHangMapper;
import com.crm.persistence.repositories.CoHoiBanHangJpaRepo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Repository
public class CoHoiBanHangJpaRepoIml implements CoiHoiBanHangRepo {
    private final CoHoiBanHangJpaRepo repo;
    public CoHoiBanHangJpaRepoIml(CoHoiBanHangJpaRepo repo)
    {
        this.repo=repo;
    }

    @Override
    public List<CoHoiBanHang> findAll() {
        return repo.findByIsDeleted(0).stream()
                .map(CoHoiBanHangMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<CoHoiBanHang> findById(Integer id) {
        return repo.findByIdAndIsDeleted(id,0).map(CoHoiBanHangMapper::toDomain);
    }

    @Override
    public CoHoiBanHang save(CoHoiBanHang cohoi) {
        return CoHoiBanHangMapper.toDomain(repo.save(CoHoiBanHangMapper.toJpa(cohoi)));
    }

    @Override
    public CoHoiBanHang update(Integer id, CoHoiBanHang cohoi) {
        CoHoiBanHangJpaEntity existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy: " + id));

        // ✅ Chỉ update các field cần thiết, giữ nguyên CreatedAt, IsDeleted
        existing.setTenThuongVu(cohoi.getTenThuongVu());
        existing.setGiaiDoan(cohoi.getGiaiDoan());
        existing.setKhachHang_Id(cohoi.getKhachHang_Id());
        existing.setLead_Id(cohoi.getLead_Id());
        existing.setTyLeThanhCong(cohoi.getTyLeThanhCong());
        existing.setDoanhThuKyVong(cohoi.getDoanhThuKyVong());
        existing.setGhiChu(cohoi.getGhiChu());
        existing.setNgayDuKien(cohoi.getNgayDuKien());
        existing.setNhanVienPhuTrach_Id(cohoi.getNhanVienPhuTrach_Id()  );
        existing.setUpdatedAt(LocalDateTime.now());

        return CoHoiBanHangMapper.toDomain(repo.save(existing));
    }

    @Override
    public void delete(CoHoiBanHang cohoi) {
            repo.deleteById(cohoi.getId());
    }
}

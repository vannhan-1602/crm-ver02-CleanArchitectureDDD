package com.crm.persistence.adapters;

import com.crm.domain.entities.NhanVien;
import com.crm.domain.repositories.NhanVienRepository;
import com.crm.persistence.repositories.HtUserJPARepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NhanVienRepoImpl implements NhanVienRepository {
    private final HtUserJPARepo htUserJPARepo;
    public NhanVienRepoImpl(HtUserJPARepo htUserJPARepo){
        this.htUserJPARepo=htUserJPARepo;
    }
    @Override
    public Optional<String> findHoTenById(Integer userId){
        if (userId==null) return Optional.empty();
        return htUserJPARepo.findHoTenByUserId(userId);
    }
    @Override
    public List<NhanVien> findAll() {
        return htUserJPARepo.findAllWithHoTen()
                .stream()
                .map(p -> new NhanVien(p.getId(), p.getHoTen()))
                .toList();
    }
}

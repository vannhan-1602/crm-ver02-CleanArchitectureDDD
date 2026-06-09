package com.crm.persistence.adapters;

import com.crm.domain.entities.Lead;
import com.crm.domain.repositories.LeadRepo;
import com.crm.persistence.mapper.LeadMapper;
import com.crm.persistence.repositories.LeadJPARepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LeadJpaRepoImpl implements LeadRepo {

    private final LeadJPARepo jpaRepo;

    public LeadJpaRepoImpl(LeadJPARepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Lead save(Lead lead) {
        return LeadMapper.toDomain(jpaRepo.save(LeadMapper.toJpa(lead)));
    }

    @Override
    public Optional<Lead> findById(Long id) {
        return jpaRepo.findByIdAndIsDeletedFalse(id)
                .map(LeadMapper::toDomain);
    }

    @Override
    public List<Lead> findAll() {
        return jpaRepo.findByIsDeletedFalse()
                .stream()
                .map(LeadMapper::toDomain)
                .toList();
    }

    @Override
    public List<Lead> findByNhanVienPhuTrachId(Integer nhanVienId) {
        return jpaRepo.findByNhanVienPhuTrachIdAndIsDeletedFalse(nhanVienId)
                .stream()
                .map(LeadMapper::toDomain)
                .toList();
    }

    @Override
    public void softDeleteById(Long id) {
        jpaRepo.softDeleteById(id);
    }
}
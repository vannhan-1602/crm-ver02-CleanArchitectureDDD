package com.crm.persistence.adapters;

import com.crm.domain.entities.LoaiTicket;
import com.crm.domain.repositories.LoaiTicketRepo;
import com.crm.persistence.mapper.LoaiTicketMapper;
import com.crm.persistence.repositories.LoaiTicketJPARepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LoaiTicketJpaRepoImpl implements LoaiTicketRepo {

    private final LoaiTicketJPARepo jpaRepo;

    public LoaiTicketJpaRepoImpl(LoaiTicketJPARepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public LoaiTicket save(LoaiTicket loaiTicket) {
        return LoaiTicketMapper.toDomain(jpaRepo.save(LoaiTicketMapper.toJpa(loaiTicket)));
    }

    @Override
    public Optional<LoaiTicket> findById(Short id) {
        return jpaRepo.findById(id).map(LoaiTicketMapper::toDomain);
    }

    @Override
    public List<LoaiTicket> findAll() {
        return jpaRepo.findAll().stream().map(LoaiTicketMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Short id) {
        jpaRepo.deleteById(id);
    }
}

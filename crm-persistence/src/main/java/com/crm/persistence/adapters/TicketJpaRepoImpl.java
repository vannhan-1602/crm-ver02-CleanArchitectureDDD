package com.crm.persistence.adapters;

import com.crm.domain.entities.Ticket;
import com.crm.domain.repositories.TicketRepo;
import com.crm.persistence.mapper.TicketMapper;
import com.crm.persistence.repositories.TicketJPARepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TicketJpaRepoImpl implements TicketRepo {

    private final TicketJPARepo jpaRepo;

    public TicketJpaRepoImpl(TicketJPARepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Ticket save(Ticket ticket) {
        return TicketMapper.toDomain(jpaRepo.save(TicketMapper.toJpa(ticket)));
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return jpaRepo.findById(id)
                .filter(jpa -> !Boolean.TRUE.equals(jpa.getIsDeleted()))
                .map(TicketMapper::toDomain);
    }

    @Override
    public List<Ticket> findAll() {
        return jpaRepo.findByIsDeletedFalse().stream().map(TicketMapper::toDomain).toList();
    }

    @Override
    public List<Ticket> findByKhachHangId(Long khachHangId) {
        return jpaRepo.findByKhachHangIdAndIsDeletedFalse(khachHangId)
                .stream()
                .map(TicketMapper::toDomain)
                .toList();
    }

    @Override
    public List<Ticket> findByTrangThai(String trangThai) {
        return jpaRepo.findByTrangThaiAndIsDeletedFalse(trangThai)
                .stream()
                .map(TicketMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Ticket> findByMaTicket(String maTicket) {
        return jpaRepo.findByMaTicket(maTicket)
                .filter(jpa -> !Boolean.TRUE.equals(jpa.getIsDeleted()))
                .map(TicketMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }
}

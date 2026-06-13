package com.crm.persistence.adapters;

import com.crm.domain.entities.TicketPhanHoi;
import com.crm.domain.repositories.TicketPhanHoiRepo;
import com.crm.persistence.mapper.TicketPhanHoiMapper;
import com.crm.persistence.repositories.TicketPhanHoiJPARepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TicketPhanHoiJpaRepoImpl implements TicketPhanHoiRepo {

    private final TicketPhanHoiJPARepo jpaRepo;

    public TicketPhanHoiJpaRepoImpl(TicketPhanHoiJPARepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public TicketPhanHoi save(TicketPhanHoi phanHoi) {
        return TicketPhanHoiMapper.toDomain(jpaRepo.save(TicketPhanHoiMapper.toJpa(phanHoi)));
    }

    @Override
    public Optional<TicketPhanHoi> findById(Long id) {
        return jpaRepo.findById(id).map(TicketPhanHoiMapper::toDomain);
    }

    @Override
    public List<TicketPhanHoi> findAll() {
        return jpaRepo.findAll().stream().map(TicketPhanHoiMapper::toDomain).toList();
    }

    @Override
    public List<TicketPhanHoi> findByTicketId(Long ticketId) {
        return jpaRepo.findByTicketIdOrderByCreatedAtAsc(ticketId)
                .stream()
                .map(TicketPhanHoiMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }
}

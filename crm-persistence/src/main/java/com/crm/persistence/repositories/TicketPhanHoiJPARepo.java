package com.crm.persistence.repositories;

import com.crm.persistence.jpa.TicketPhanHoiJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketPhanHoiJPARepo extends JpaRepository<TicketPhanHoiJpaEntity, Long> {
    List<TicketPhanHoiJpaEntity> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}

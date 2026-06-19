package com.crm.persistence.repositories;

import com.crm.persistence.jpa.LoaiTicketJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiTicketJPARepo extends JpaRepository<LoaiTicketJpaEntity, Short> {
}

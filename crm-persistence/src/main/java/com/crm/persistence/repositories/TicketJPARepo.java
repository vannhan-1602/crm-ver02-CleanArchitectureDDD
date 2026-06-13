package com.crm.persistence.repositories;

import com.crm.persistence.jpa.TicketJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketJPARepo extends JpaRepository<TicketJpaEntity, Long> {
    List<TicketJpaEntity> findByIsDeletedFalse();
    List<TicketJpaEntity> findByKhachHangIdAndIsDeletedFalse(Long khachHangId);
    List<TicketJpaEntity> findByTrangThaiAndIsDeletedFalse(String trangThai);
    Optional<TicketJpaEntity> findByMaTicket(String maTicket);
}

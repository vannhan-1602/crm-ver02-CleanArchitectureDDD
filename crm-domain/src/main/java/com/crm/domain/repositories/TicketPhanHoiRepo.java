package com.crm.domain.repositories;

import com.crm.domain.entities.TicketPhanHoi;

import java.util.List;
import java.util.Optional;

public interface TicketPhanHoiRepo {
    TicketPhanHoi save(TicketPhanHoi phanHoi);
    Optional<TicketPhanHoi> findById(Long id);
    List<TicketPhanHoi> findAll();
    List<TicketPhanHoi> findByTicketId(Long ticketId);
    void deleteById(Long id);
}

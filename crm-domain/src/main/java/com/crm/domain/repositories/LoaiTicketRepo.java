package com.crm.domain.repositories;

import com.crm.domain.entities.LoaiTicket;

import java.util.List;
import java.util.Optional;

public interface LoaiTicketRepo {
    LoaiTicket save(LoaiTicket loaiTicket);
    Optional<LoaiTicket> findById(Short id);
    List<LoaiTicket> findAll();
    void deleteById(Short id);
}

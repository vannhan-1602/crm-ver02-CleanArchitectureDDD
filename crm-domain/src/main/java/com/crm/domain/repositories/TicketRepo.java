package com.crm.domain.repositories;

import com.crm.domain.entities.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepo {
    Ticket save(Ticket ticket);
    Optional<Ticket> findById(Long id);
    List<Ticket> findAll();
    List<Ticket> findByKhachHangId(Long khachHangId);
    List<Ticket> findByTrangThai(String trangThai);
    Optional<Ticket> findByMaTicket(String maTicket);
    void deleteById(Long id);
}

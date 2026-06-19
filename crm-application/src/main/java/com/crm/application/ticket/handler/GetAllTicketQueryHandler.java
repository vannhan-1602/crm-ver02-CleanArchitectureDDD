package com.crm.application.ticket.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.ticket.query.GetAllTicketQuery;
import com.crm.domain.entities.Ticket;
import com.crm.domain.repositories.TicketRepo;
import com.crm.domain.valueobjects.TrangThaiTicket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTicketQueryHandler implements IRequestHandler<GetAllTicketQuery, List<Ticket>> {
    private final TicketRepo repo;

    public GetAllTicketQueryHandler(TicketRepo repo) { this.repo = repo; }

    @Override
    public List<Ticket> handle(GetAllTicketQuery query) {
        if (query.getKhachHangId() != null) {
            return repo.findByKhachHangId(query.getKhachHangId());
        }
        if (query.getTrangThai() != null && !query.getTrangThai().isBlank()) {
            return repo.findByTrangThai(TrangThaiTicket.from(query.getTrangThai()).name());
        }
        return repo.findAll();
    }
}

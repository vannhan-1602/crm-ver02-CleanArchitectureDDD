package com.crm.application.ticketphanhoi.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.ticketphanhoi.query.GetAllTicketPhanHoiQuery;
import com.crm.domain.entities.TicketPhanHoi;
import com.crm.domain.repositories.TicketPhanHoiRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTicketPhanHoiQueryHandler implements IRequestHandler<GetAllTicketPhanHoiQuery, List<TicketPhanHoi>> {
    private final TicketPhanHoiRepo repo;

    public GetAllTicketPhanHoiQueryHandler(TicketPhanHoiRepo repo) { this.repo = repo; }

    @Override
    public List<TicketPhanHoi> handle(GetAllTicketPhanHoiQuery query) {
        if (query.getTicketId() != null) {
            return repo.findByTicketId(query.getTicketId());
        }
        return repo.findAll();
    }
}

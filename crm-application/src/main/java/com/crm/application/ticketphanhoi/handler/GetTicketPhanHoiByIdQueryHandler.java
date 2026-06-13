package com.crm.application.ticketphanhoi.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.ticketphanhoi.query.GetTicketPhanHoiByIdQuery;
import com.crm.domain.entities.TicketPhanHoi;
import com.crm.domain.repositories.TicketPhanHoiRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class GetTicketPhanHoiByIdQueryHandler implements IRequestHandler<GetTicketPhanHoiByIdQuery, TicketPhanHoi> {
    private final TicketPhanHoiRepo repo;

    public GetTicketPhanHoiByIdQueryHandler(TicketPhanHoiRepo repo) { this.repo = repo; }

    @Override
    public TicketPhanHoi handle(GetTicketPhanHoiByIdQuery query) {
        return repo.findById(query.getId())
                .orElseThrow(() -> new NoSuchElementException("TicketPhanHoi khong ton tai: " + query.getId()));
    }
}

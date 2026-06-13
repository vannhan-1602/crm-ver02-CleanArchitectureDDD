package com.crm.application.ticket.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.ticket.query.GetTicketByIdQuery;
import com.crm.domain.entities.Ticket;
import com.crm.domain.repositories.TicketRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class GetTicketByIdQueryHandler implements IRequestHandler<GetTicketByIdQuery, Ticket> {
    private final TicketRepo repo;

    public GetTicketByIdQueryHandler(TicketRepo repo) { this.repo = repo; }

    @Override
    public Ticket handle(GetTicketByIdQuery query) {
        return repo.findById(query.getId())
                .orElseThrow(() -> new NoSuchElementException("Ticket khong ton tai: " + query.getId()));
    }
}

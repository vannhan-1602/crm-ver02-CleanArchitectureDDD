package com.crm.application.ticket.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.Ticket;

public class GetTicketByIdQuery implements IRequest<Ticket> {
    private Long id;

    public GetTicketByIdQuery(Long id) { this.id = id; }

    public Long getId() { return id; }
}

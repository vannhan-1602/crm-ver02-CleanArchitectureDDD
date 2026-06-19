package com.crm.application.ticketphanhoi.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.TicketPhanHoi;

import java.util.List;

public class GetAllTicketPhanHoiQuery implements IRequest<List<TicketPhanHoi>> {
    private Long ticketId;

    public GetAllTicketPhanHoiQuery(Long ticketId) { this.ticketId = ticketId; }

    public Long getTicketId() { return ticketId; }
}

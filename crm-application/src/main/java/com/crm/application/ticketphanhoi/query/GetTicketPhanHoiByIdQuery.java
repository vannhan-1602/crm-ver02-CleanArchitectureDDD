package com.crm.application.ticketphanhoi.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.TicketPhanHoi;

public class GetTicketPhanHoiByIdQuery implements IRequest<TicketPhanHoi> {
    private Long id;

    public GetTicketPhanHoiByIdQuery(Long id) { this.id = id; }

    public Long getId() { return id; }
}

package com.crm.application.loaiticket.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.LoaiTicket;

public class GetLoaiTicketByIdQuery implements IRequest<LoaiTicket> {
    private Short id;

    public GetLoaiTicketByIdQuery(Short id) { this.id = id; }

    public Short getId() { return id; }
}

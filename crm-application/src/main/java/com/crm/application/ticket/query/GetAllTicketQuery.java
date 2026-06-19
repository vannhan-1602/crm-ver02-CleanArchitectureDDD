package com.crm.application.ticket.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.Ticket;

import java.util.List;

public class GetAllTicketQuery implements IRequest<List<Ticket>> {
    private Long khachHangId;
    private String trangThai;

    public GetAllTicketQuery(Long khachHangId, String trangThai) {
        this.khachHangId = khachHangId;
        this.trangThai = trangThai;
    }

    public Long getKhachHangId() { return khachHangId; }
    public String getTrangThai() { return trangThai; }
}

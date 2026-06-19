package com.crm.application.ticketphanhoi.command;

import com.crm.application.common.IRequest;

public class DeleteTicketPhanHoiCommand implements IRequest<Boolean> {
    private Long id;

    public DeleteTicketPhanHoiCommand(Long id) { this.id = id; }

    public Long getId() { return id; }
}

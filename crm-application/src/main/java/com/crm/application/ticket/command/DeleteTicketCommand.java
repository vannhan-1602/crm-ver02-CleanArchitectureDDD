package com.crm.application.ticket.command;

import com.crm.application.common.IRequest;

public class DeleteTicketCommand implements IRequest<Boolean> {
    private Long id;

    public DeleteTicketCommand(Long id) { this.id = id; }

    public Long getId() { return id; }
}

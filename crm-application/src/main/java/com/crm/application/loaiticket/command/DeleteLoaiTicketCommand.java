package com.crm.application.loaiticket.command;

import com.crm.application.common.IRequest;

public class DeleteLoaiTicketCommand implements IRequest<Boolean> {
    private Short id;

    public DeleteLoaiTicketCommand(Short id) { this.id = id; }

    public Short getId() { return id; }
}

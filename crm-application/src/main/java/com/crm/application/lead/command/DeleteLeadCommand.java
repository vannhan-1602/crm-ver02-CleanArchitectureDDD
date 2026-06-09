package com.crm.application.lead.command;

import com.crm.application.common.IRequest;


public class DeleteLeadCommand implements IRequest<Boolean> {

    private Long id;

    public DeleteLeadCommand(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
}
package com.crm.application.baogia.command;

import com.crm.application.common.IRequest;

public class DeleteBaoGiaCommand implements IRequest<Void> {
    private final Long id;

    public DeleteBaoGiaCommand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

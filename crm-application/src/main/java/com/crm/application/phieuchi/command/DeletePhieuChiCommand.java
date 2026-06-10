package com.crm.application.phieuchi.command;

import com.crm.application.common.IRequest;

public class DeletePhieuChiCommand implements IRequest<Boolean> {
    private Long id;

    public DeletePhieuChiCommand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

package com.crm.application.hoadon.command;

import com.crm.application.common.IRequest;

public class DeleteHoaDonCommand implements IRequest<Boolean> {
    private Long id;

    public DeleteHoaDonCommand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

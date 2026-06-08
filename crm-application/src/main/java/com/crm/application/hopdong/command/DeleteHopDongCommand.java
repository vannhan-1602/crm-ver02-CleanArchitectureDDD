package com.crm.application.hopdong.command;

import com.crm.application.common.IRequest;

public class DeleteHopDongCommand implements IRequest<Boolean> {
    private Long id;
    public DeleteHopDongCommand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

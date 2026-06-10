package com.crm.application.hopdong.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.HopDong;

public class CompleteHopDongCommand implements IRequest<HopDong> {
    private Long id;

    public CompleteHopDongCommand() {
    }

    public CompleteHopDongCommand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

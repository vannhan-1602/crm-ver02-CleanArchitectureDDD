package com.crm.application.phieuthu.command;

import com.crm.application.common.IRequest;

public class DeletePhieuThuCommand implements IRequest<Boolean> {
    private Long id;

    public DeletePhieuThuCommand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

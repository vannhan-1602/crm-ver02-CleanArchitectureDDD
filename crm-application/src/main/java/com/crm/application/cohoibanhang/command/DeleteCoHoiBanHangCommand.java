package com.crm.application.cohoibanhang.command;

import com.crm.application.common.IRequest;

public class DeleteCoHoiBanHangCommand implements IRequest<Boolean> {

    private final Integer id;

    public DeleteCoHoiBanHangCommand(Integer id) {
        this.id = id;
    }

    public Integer getId() { return id; }
}
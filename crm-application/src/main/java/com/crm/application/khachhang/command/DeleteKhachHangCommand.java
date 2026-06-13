package com.crm.application.khachhang.command;

import com.crm.application.common.IRequest;

public class DeleteKhachHangCommand implements IRequest<Boolean> {

    private Long id;

    public DeleteKhachHangCommand(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
}
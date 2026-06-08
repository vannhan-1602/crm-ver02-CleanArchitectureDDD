package com.crm.application.sanpham.command;

import com.crm.application.common.IRequest;

public class DeleteSanPhamCommand implements IRequest<Boolean> {
    private final Integer  id;

    public DeleteSanPhamCommand(Integer  id) { this.id = id; }

    public Integer  getId() { return id; }
}

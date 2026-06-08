package com.crm.application.sanpham.command;

import com.crm.application.common.IRequest;

import java.util.UUID;

public class DeleteSanPhamCommand implements IRequest<Boolean> {
    private final Integer  id;

    public DeleteSanPhamCommand(Integer  id) { this.id = id; }

    public Integer  getId() { return id; }
}
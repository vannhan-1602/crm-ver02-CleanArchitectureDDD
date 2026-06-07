package com.crm.application.sanpham.command;

import java.util.UUID;

public class DeleteSanPhamCommand {
    private Integer  id;

    public DeleteSanPhamCommand(Integer  id) { this.id = id; }

    public Integer  getId() { return id; }
}
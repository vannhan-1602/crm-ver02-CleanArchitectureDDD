package com.crm.application.loaisanpham.command;

import java.util.UUID;

public class DeleteLoaiSanPhamCommand {
    private Integer  id;
    public DeleteLoaiSanPhamCommand(Integer  id) { this.id = id; }
    public Integer getId() { return id; }
}
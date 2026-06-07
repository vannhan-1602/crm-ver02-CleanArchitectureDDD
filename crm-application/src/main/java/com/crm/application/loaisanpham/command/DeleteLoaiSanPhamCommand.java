package com.crm.application.loaisanpham.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.LoaiSanPham;

import java.util.List;
import java.util.UUID;

public class DeleteLoaiSanPhamCommand implements IRequest<Boolean> {
    private Integer  id;
    public DeleteLoaiSanPhamCommand(Integer  id) { this.id = id; }
    public Integer getId() { return id; }
}
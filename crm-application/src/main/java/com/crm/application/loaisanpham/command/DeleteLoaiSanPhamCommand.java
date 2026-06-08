package com.crm.application.loaisanpham.command;

import com.crm.application.common.IRequest;
public class DeleteLoaiSanPhamCommand implements IRequest<Boolean> {
    private Integer  id;
    public DeleteLoaiSanPhamCommand(Integer  id) { this.id = id; }
    public Integer getId() { return id; }
}

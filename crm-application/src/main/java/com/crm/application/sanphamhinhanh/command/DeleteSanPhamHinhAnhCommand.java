package com.crm.application.sanphamhinhanh.command;

import com.crm.application.common.IRequest;
public class DeleteSanPhamHinhAnhCommand implements IRequest<Boolean> {
    private Integer  id;
    public DeleteSanPhamHinhAnhCommand(Integer  id) { this.id = id; }
    public Integer getId() { return id; }
}

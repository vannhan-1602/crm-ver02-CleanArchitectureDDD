package com.crm.application.loaisanpham.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.LoaiSanPham;

public class GetLoaiSanPhamByIdQuery implements IRequest<LoaiSanPham> {
    private Integer  id;
    public GetLoaiSanPhamByIdQuery(Integer  id) { this.id = id; }
    public Integer getId() { return id; }
}

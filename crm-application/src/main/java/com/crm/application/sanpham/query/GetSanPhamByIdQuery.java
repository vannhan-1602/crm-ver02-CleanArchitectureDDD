package com.crm.application.sanpham.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.SanPham;

public class GetSanPhamByIdQuery implements IRequest<SanPham> {
    private final   Integer  id;

    public GetSanPhamByIdQuery(Integer  id) { this.id = id; }

    public Integer  getId() { return id; }
}

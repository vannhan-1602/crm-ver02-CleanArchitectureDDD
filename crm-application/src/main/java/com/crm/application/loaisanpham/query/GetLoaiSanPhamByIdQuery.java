package com.crm.application.loaisanpham.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.LoaiSanPham;

import java.util.List;
import java.util.UUID;

public class GetLoaiSanPhamByIdQuery implements IRequest<LoaiSanPham> {
    private Integer  id;
    public GetLoaiSanPhamByIdQuery(Integer  id) { this.id = id; }
    public Integer getId() { return id; }
}
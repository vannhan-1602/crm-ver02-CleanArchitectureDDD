package com.crm.application.loaisanpham.query;

import java.util.UUID;

public class GetLoaiSanPhamByIdQuery {
    private Integer  id;
    public GetLoaiSanPhamByIdQuery(Integer  id) { this.id = id; }
    public Integer getId() { return id; }
}
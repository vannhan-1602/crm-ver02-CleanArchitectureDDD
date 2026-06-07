package com.crm.application.sanpham.query;

import java.util.UUID;

public class GetSanPhamByIdQuery {
    private Integer  id;

    public GetSanPhamByIdQuery(Integer  id) { this.id = id; }

    public Integer  getId() { return id; }
}
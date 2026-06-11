package com.crm.application.khachhang.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.KhachHang;

public class GetKhachHangByIdQuery implements IRequest<KhachHang> {

    private Long id;

    public GetKhachHangByIdQuery() {}

    public GetKhachHangByIdQuery(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
}
package com.crm.application.cohoibanhang.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.CoHoiBanHang;

public class GetCoHoiBanHangByIdQuery implements IRequest<CoHoiBanHang> {

    private final Integer id;

    public GetCoHoiBanHangByIdQuery(Integer id) {
        this.id = id;
    }

    public Integer getId() { return id; }
}
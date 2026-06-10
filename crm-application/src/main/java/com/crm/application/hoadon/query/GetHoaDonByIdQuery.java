package com.crm.application.hoadon.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.HoaDon;

public class GetHoaDonByIdQuery implements IRequest<HoaDon> {
    private Long id;

    public GetHoaDonByIdQuery(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

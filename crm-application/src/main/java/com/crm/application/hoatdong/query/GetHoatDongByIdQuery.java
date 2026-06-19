package com.crm.application.hoatdong.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.HoatDong;

public class GetHoatDongByIdQuery implements IRequest<HoatDong> {

    private Long id;

    public GetHoatDongByIdQuery() {}

    public GetHoatDongByIdQuery(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
}
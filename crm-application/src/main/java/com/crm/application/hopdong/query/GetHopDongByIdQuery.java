package com.crm.application.hopdong.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.HopDong;

public class GetHopDongByIdQuery implements IRequest<HopDong> {
    private Long id;

    public GetHopDongByIdQuery() {
    }

    public GetHopDongByIdQuery(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

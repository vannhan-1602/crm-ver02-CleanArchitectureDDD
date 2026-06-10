package com.crm.application.baogia.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.BaoGia;

public class GetBaoGiaByIdQuery implements IRequest<BaoGia> {
    private final Long id;

    public GetBaoGiaByIdQuery(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

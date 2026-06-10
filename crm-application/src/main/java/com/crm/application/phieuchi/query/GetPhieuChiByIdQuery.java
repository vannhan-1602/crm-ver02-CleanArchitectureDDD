package com.crm.application.phieuchi.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.PhieuChi;

public class GetPhieuChiByIdQuery implements IRequest<PhieuChi> {
    private Long id;

    public GetPhieuChiByIdQuery(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

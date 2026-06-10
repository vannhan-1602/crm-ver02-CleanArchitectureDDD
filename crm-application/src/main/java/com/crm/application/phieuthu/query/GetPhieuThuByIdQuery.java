package com.crm.application.phieuthu.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.PhieuThu;

public class GetPhieuThuByIdQuery implements IRequest<PhieuThu> {
    private Long id;

    public GetPhieuThuByIdQuery(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

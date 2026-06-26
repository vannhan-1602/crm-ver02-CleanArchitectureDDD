package com.crm.application.sanphamhinhanh.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.SanPhamHinhAnh;

public class SetMainSanPhamHinhAnhCommand implements IRequest<SanPhamHinhAnh> {
    private final Integer sanPhamId;
    private final Integer hinhAnhId;

    public SetMainSanPhamHinhAnhCommand(Integer sanPhamId, Integer hinhAnhId) {
        this.sanPhamId = sanPhamId;
        this.hinhAnhId = hinhAnhId;
    }

    public Integer getSanPhamId() {
        return sanPhamId;
    }

    public Integer getHinhAnhId() {
        return hinhAnhId;
    }
}

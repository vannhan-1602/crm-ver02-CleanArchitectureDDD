package com.crm.application.sanphamhinhanh.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.entities.SanPhamHinhAnh;

public class GetSanPhamHinhAnhByIdQuery implements IRequest<SanPhamHinhAnh> {
    private Integer  id;
    public GetSanPhamHinhAnhByIdQuery(Integer  id) { this.id = id; }
    public Integer getId() { return id; }
}

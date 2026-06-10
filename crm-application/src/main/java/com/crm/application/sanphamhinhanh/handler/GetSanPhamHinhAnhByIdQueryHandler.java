package com.crm.application.sanphamhinhanh.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanphamhinhanh.query.GetSanPhamHinhAnhByIdQuery;
import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.entities.SanPhamHinhAnh;
import com.crm.domain.repositories.LoaiSanPhamRepo;
import com.crm.domain.repositories.SanPhamHinhAnhRepo;
import org.springframework.stereotype.Service;

@Service
public class GetSanPhamHinhAnhByIdQueryHandler
        implements IRequestHandler<GetSanPhamHinhAnhByIdQuery, SanPhamHinhAnh> {
    private final SanPhamHinhAnhRepo loaiSanPhamRepo;

    public GetSanPhamHinhAnhByIdQueryHandler(SanPhamHinhAnhRepo loaiSanPhamRepo) {
        this.loaiSanPhamRepo = loaiSanPhamRepo;
    }

    @Override
    public SanPhamHinhAnh handle(GetSanPhamHinhAnhByIdQuery query) {
        return loaiSanPhamRepo.findById(query.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hinh  sản phẩm"));
    }
}

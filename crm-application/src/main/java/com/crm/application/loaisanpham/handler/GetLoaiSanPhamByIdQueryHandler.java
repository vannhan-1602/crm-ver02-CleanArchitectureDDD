package com.crm.application.loaisanpham.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.loaisanpham.query.GetLoaiSanPhamByIdQuery;
import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.repositories.LoaiSanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class GetLoaiSanPhamByIdQueryHandler
        implements IRequestHandler<GetLoaiSanPhamByIdQuery, LoaiSanPham> {
    private final LoaiSanPhamRepo loaiSanPhamRepo;

    public GetLoaiSanPhamByIdQueryHandler(LoaiSanPhamRepo loaiSanPhamRepo) {
        this.loaiSanPhamRepo = loaiSanPhamRepo;
    }

    @Override
    public LoaiSanPham handle(GetLoaiSanPhamByIdQuery query) {
        return loaiSanPhamRepo.findById(query.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại sản phẩm"));
    }
}

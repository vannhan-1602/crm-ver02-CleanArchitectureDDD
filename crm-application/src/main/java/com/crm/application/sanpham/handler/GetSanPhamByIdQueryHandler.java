package com.crm.application.sanpham.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanpham.query.GetSanPhamByIdQuery;
import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class GetSanPhamByIdQueryHandler
        implements IRequestHandler<GetSanPhamByIdQuery, SanPham> {

    private final SanPhamRepo sanPhamRepo;

    public GetSanPhamByIdQueryHandler(SanPhamRepo sanPhamRepo) {
        this.sanPhamRepo = sanPhamRepo;
    }

    @Override
    public SanPham handle(GetSanPhamByIdQuery request) {
        return sanPhamRepo.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }
}
package com.crm.application.sanpham.handler;

import com.crm.application.sanpham.query.GetSanPhamByIdQuery;
import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class GetSanPhamByIdQueryHandler {
    private final SanPhamRepo sanPhamRepo;

    public GetSanPhamByIdQueryHandler(SanPhamRepo sanPhamRepo) {
        this.sanPhamRepo = sanPhamRepo;
    }

    public SanPham handle(GetSanPhamByIdQuery query) {
        return sanPhamRepo.findById(query.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }
}
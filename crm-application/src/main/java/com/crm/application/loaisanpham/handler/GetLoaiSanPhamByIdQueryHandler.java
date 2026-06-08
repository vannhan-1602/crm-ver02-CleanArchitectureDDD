package com.crm.application.loaisanpham.handler;

import com.crm.application.common.IRequest;
import com.crm.application.loaisanpham.query.GetLoaiSanPhamByIdQuery;
import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.repositories.LoaiSanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class GetLoaiSanPhamByIdQueryHandler implements IRequest<GetLoaiSanPhamByIdQuery> {
    private final LoaiSanPhamRepo loaiSanPhamRepo;

    public GetLoaiSanPhamByIdQueryHandler(LoaiSanPhamRepo loaiSanPhamRepo) {
        this.loaiSanPhamRepo = loaiSanPhamRepo;
    }

    public LoaiSanPham handle(GetLoaiSanPhamByIdQuery query) {
        return loaiSanPhamRepo.findById(query.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại sản phẩm"));
    }
}
package com.crm.application.loaisanpham.handler;

import com.crm.application.loaisanpham.query.GetAllLoaiSanPhamQuery;
import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.repositories.LoaiSanPhamRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllLoaiSanPhamQueryHandler {
    private final LoaiSanPhamRepo loaiSanPhamRepo;

    public GetAllLoaiSanPhamQueryHandler(LoaiSanPhamRepo loaiSanPhamRepo) {
        this.loaiSanPhamRepo = loaiSanPhamRepo;
    }

    public List<LoaiSanPham> handle(GetAllLoaiSanPhamQuery query) {
        return loaiSanPhamRepo.findAll();
    }
}
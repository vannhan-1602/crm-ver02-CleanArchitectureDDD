package com.crm.application.sanpham.handler;

import com.crm.application.sanpham.query.GetAllSanPhamQuery;
import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllSanPhamQueryHandler {
    private final SanPhamRepo sanPhamRepo;

    public GetAllSanPhamQueryHandler(SanPhamRepo sanPhamRepo) {
        this.sanPhamRepo = sanPhamRepo;
    }

    public List<SanPham> handle(GetAllSanPhamQuery query) {
        return sanPhamRepo.findAll();
    }
}
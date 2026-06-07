package com.crm.application.sanpham.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanpham.query.GetAllSanPhamQuery;
import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllSanPhamQueryHandler
        implements IRequestHandler<GetAllSanPhamQuery, List<SanPham>> {

    private final SanPhamRepo sanPhamRepo;

    public GetAllSanPhamQueryHandler(SanPhamRepo sanPhamRepo) {
        this.sanPhamRepo = sanPhamRepo;
    }

    @Override
    public List<SanPham> handle(GetAllSanPhamQuery request) {
        return sanPhamRepo.findAll();
    }
}
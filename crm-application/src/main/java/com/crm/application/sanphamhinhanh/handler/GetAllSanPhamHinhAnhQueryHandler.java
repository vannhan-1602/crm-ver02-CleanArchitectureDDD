package com.crm.application.sanphamhinhanh.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanphamhinhanh.query.GetAllSanPhamHinhAnh;
import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.entities.SanPhamHinhAnh;
import com.crm.domain.repositories.LoaiSanPhamRepo;
import com.crm.domain.repositories.SanPhamHinhAnhRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllSanPhamHinhAnhQueryHandler
        implements IRequestHandler<GetAllSanPhamHinhAnh, List<SanPhamHinhAnh>> {
    private final SanPhamHinhAnhRepo repo;

    public GetAllSanPhamHinhAnhQueryHandler(SanPhamHinhAnhRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<SanPhamHinhAnh> handle(GetAllSanPhamHinhAnh query) {
        return repo.findAll();
    }
}

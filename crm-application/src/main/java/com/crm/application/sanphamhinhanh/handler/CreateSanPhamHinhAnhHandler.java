package com.crm.application.sanphamhinhanh.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanphamhinhanh.command.CreateSanPhamHinhAnhCommand;
import com.crm.domain.entities.SanPhamHinhAnh;
import com.crm.domain.repositories.SanPhamHinhAnhRepo;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class CreateSanPhamHinhAnhHandler implements IRequestHandler<CreateSanPhamHinhAnhCommand, SanPhamHinhAnh> {
    private final SanPhamHinhAnhRepo sanPhamHinhAnhRepo;
    private final SanPhamRepo sanPhamRepo;

    public CreateSanPhamHinhAnhHandler(SanPhamHinhAnhRepo sanPhamHinhAnhRepo, SanPhamRepo sanPhamRepo) {
        this.sanPhamHinhAnhRepo = sanPhamHinhAnhRepo;
        this.sanPhamRepo = sanPhamRepo;
    }

    @Override
    public SanPhamHinhAnh handle(CreateSanPhamHinhAnhCommand command) {
        sanPhamRepo.findById(command.getSanPham_Id())
                .orElseThrow(() -> new RuntimeException("Khong tim thay san pham"));

        SanPhamHinhAnh hinhAnh = new SanPhamHinhAnh();
        hinhAnh.setSanPham_Id(command.getSanPham_Id());
        hinhAnh.setUrl(command.getUrl());
        hinhAnh.setIsMain(command.getIsMain());
        return sanPhamHinhAnhRepo.save(hinhAnh);
    }
}

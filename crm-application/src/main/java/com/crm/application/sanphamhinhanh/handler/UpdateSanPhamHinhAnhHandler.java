package com.crm.application.sanphamhinhanh.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanphamhinhanh.command.UpdateSanPhamHinhAnhCommand;
import com.crm.domain.entities.SanPhamHinhAnh;
import com.crm.domain.repositories.SanPhamHinhAnhRepo;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class UpdateSanPhamHinhAnhHandler
        implements IRequestHandler<UpdateSanPhamHinhAnhCommand, SanPhamHinhAnh> {
    private final SanPhamHinhAnhRepo sanPhamHinhAnhRepo;
    private final SanPhamRepo sanPhamRepo;

    public UpdateSanPhamHinhAnhHandler(SanPhamHinhAnhRepo sanPhamHinhAnhRepo, SanPhamRepo sanPhamRepo) {
        this.sanPhamHinhAnhRepo = sanPhamHinhAnhRepo;
        this.sanPhamRepo = sanPhamRepo;
    }

    @Override
    public SanPhamHinhAnh handle(UpdateSanPhamHinhAnhCommand command) {
        SanPhamHinhAnh hinhAnh = sanPhamHinhAnhRepo.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay hinh anh san pham"));

        sanPhamRepo.findById(command.getSanPham_Id())
                .orElseThrow(() -> new RuntimeException("Khong tim thay san pham"));

        hinhAnh.setSanPham_Id(command.getSanPham_Id());
        hinhAnh.setUrl(command.getUrl());
        hinhAnh.setIsMain(command.getIsMain());
        return sanPhamHinhAnhRepo.save(hinhAnh);
    }
}

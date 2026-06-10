package com.crm.application.sanpham.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanpham.command.AddHinhAnhToSanPhamCommand;
import com.crm.domain.entities.SanPham;
import com.crm.domain.entities.SanPhamHinhAnh;
import com.crm.domain.repositories.SanPhamHinhAnhRepo;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class AddHinhAnhToSanPhamHandler
        implements IRequestHandler<AddHinhAnhToSanPhamCommand, SanPham> {

    private final SanPhamRepo sanPhamRepo;
    private final SanPhamHinhAnhRepo sanPhamHinhAnhRepo;

    public AddHinhAnhToSanPhamHandler(SanPhamRepo sanPhamRepo, SanPhamHinhAnhRepo sanPhamHinhAnhRepo) {
        this.sanPhamRepo = sanPhamRepo;
        this.sanPhamHinhAnhRepo = sanPhamHinhAnhRepo;
    }

    @Override
    public SanPham handle(AddHinhAnhToSanPhamCommand command) {
        SanPham sanPham = sanPhamRepo.findById(command.getSanPhamId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay san pham"));

        SanPhamHinhAnh hinhAnh = new SanPhamHinhAnh();
        hinhAnh.setSanPham_Id(sanPham.getSanPhamId());
        hinhAnh.setUrl(command.getUrl());
        hinhAnh.setIsMain(command.getIsMain());

        SanPhamHinhAnh saved = sanPhamHinhAnhRepo.save(hinhAnh);
        sanPham.ThemHinhAnh(saved);
        return sanPham;
    }
}
